package com.craxiom.networksurveyplus.messages;

import java.util.Arrays;

import timber.log.Timber;

/**
 * The Diag Revealer native application reads from the /dev/diag port, and writes the output to a FIFO named pipe. Diag
 * Revealer sends exactly what is received from /dev/diag, except that it also adds a header.
 * <p>
 * Following is the comment from the diag_revealer.c file and describes the custom format used by Diag Revealer:
 * <pre>
 * This program writes to FIFO using a special packet format:
 *    type: 2-byte integer. Can be one of the following values:
 *      1: LOG
 *      2: START_LOG_FILE, indicating the creation of a new log file.
 *      3: END_LOG_FILE, indicating the end of a log file.
 *    length: 2-byte integer. The total number of bytes in this packet
 *      (excluding the type field).
 * If "type" is LOG, there are two other fields:
 *    timestamp: 8-byte double float number. A POSIX timestamp representing
 *      when this log is received from the device.
 *    payload: byte stream of variable length.
 * Otherwise, "type" contains only one field:
 *    filename: the related log file's name
 * </pre>
 * <p>
 * Worthy of note is that the `length` does not include the type or length bytes.
 *
 * @since 0.1.0
 */
public class DiagRevealerMessage
{
    private final DiagRevealerMessageHeader header;

    // The instance variables if the messageType == 1
    private long timestamp;
    public byte[] payload;

    // The instance variables if the messageType == 2 || 3
    private String fileName;

    // TODO Javadocs
    public DiagRevealerMessage(DiagRevealerMessageHeader header, long timestamp, byte[] payload)
    {
        this.header = header;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public DiagRevealerMessage(DiagRevealerMessageHeader header, String fileName)
    {
        this.header = header;
        this.fileName = fileName;
    }

    /**
     * Parses the payload of a message from the Diag Revealer C program. The payload varies depending on the messageType
     * specified in the header.
     *
     * @param messageBytes The message bytes.
     * @return null if the parsing was unsuccessful or the DiagRevealerMessage object if a message could be parsed.
     */
    public static DiagRevealerMessage parseDiagRevealerMessage(byte[] messageBytes, DiagRevealerMessageHeader header)
    {
        try
        {
            final String filename;

            if (header.messageType == 1)
            {
                // TODO we should also verify that the last byte is 0x7e, and I think we need to remove it as well.
                if (messageBytes.length < 8)
                {
                    Timber.e("The diag_revealer message did not have enough bytes for the timestamp");
                    return null;
                }

                if (messageBytes.length < header.messageLength)
                {
                    Timber.e("The diag_revealer message length (%d) was longer than the provided byte array (%d)", header.messageLength, messageBytes.length);
                    return null;
                }

                final long timestamp = ParserUtils.getLong(messageBytes, 0, java.nio.ByteOrder.LITTLE_ENDIAN);

                // The payload runs from just after the timestamp to the end of the message
                final byte[] payload = Arrays.copyOfRange(messageBytes, 8, header.messageLength);

                return new DiagRevealerMessage(header, timestamp, payload);
            } else if (header.messageType == 2 || header.messageType == 3)
            {
                // The entire payload is just the filename that is either being started (2) or ended (3)
                filename = new String(messageBytes, 0, header.messageLength);

                return new DiagRevealerMessage(header, filename);
            } else
            {
                Timber.e("Received an unknown diag_revealer messageType %d", header.messageType);
                return null;
            }
        } catch (Exception e)
        {
            Timber.e(e, "Could not parse an incoming diag_revelaer message due to an exception.");
            return null;
        }
    }

    @Override
    public String toString()
    {
        return "DiagRevealerMessage{" +
                "header=" + header +
                ", timestamp=" + timestamp +
                ", payload=" + ParserUtils.convertBytesToHexString(payload, 0, payload.length) +//Arrays.toString(payload) +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
