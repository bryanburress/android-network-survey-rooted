# Network Survey Plus Android App

[![Build Status](https://travis-ci.com/christianrowlands/android-network-survey.svg?branch=develop)](https://travis-ci.com/github/christianrowlands/android-network-survey-plus)
[![License](https://img.shields.io/badge/license-Apache%202-green.svg?style=flat)](https://github.com/christianrowlands/android-network-survey-rooted/blob/develop/LICENSE)

## What is it?
Network Survey Plus is the Rooted version of the [Network Survey Android App](https://github.com/christianrowlands/android-network-survey).


## Why?
Why do we need a rooted version of the Network Survey Android app? Well, the unrooted version can only get
access to a limited set of cellular details. With root access on certain devices with the right Qualcomm
chipset, this app can get access to detailed cellular network messages via Qualcomm Diagnostic Monitor (QCDM).

This app logs several messages coming from QCDM to a pcap file for follow on processing.


## Getting Started

To build and install the project follow the steps below:

    1) Clone the repo.
    2) Open Android Studio, and then open the root directory of the cloned repo.
    3) Connect an Android Phone (make sure debugging is enabled on the device).
    4) Install and run the app by clicking the "Play" button in Android Studio.


### Prerequisites

Install Android Studio to work on this code.


## Related Projects

This project is not alone in trying to leverage QCDM to get access to low level cellular messages. Following
are a list of other projects that might be of interest to you.
 * [QCSuper](https://github.com/P1sec/QCSuper)
 * [SnoopSnitch](https://opensource.srlabs.de/projects/snoopsnitch)


## Changelog

##### [0.1.0](https://github.com/christianrowlands/android-network-survey-rooted/releases/tag/v0.1.0) - 2020-0
 * Initial Release of Network Survey Plus


## Contact

* **Christian Rowlands** - [Craxiom](https://github.com/christianrowlands)