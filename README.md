# Scan Code WMS


## Overview
*short description*

### Features

- Change language to: English, Polish.
- The app creates its own folder in your downloaded directory.
- Determining the name of the created file with codes.
- Determination after how many scans of codes are to be saved to a new file.
- Ability to enter the initial code prefix, where only codes with this prefix will be scanned.
- Ability to remove files from a folder before scanning.
- Use auto focus and flashlight while scanning.
- Display of the number of scanned codes so far.
- Sending files to another device via Bluetooth.
- Uploading files to an SFTP server.

### Receiving files via Windows

If you want to send files via Bluetooth to a Windows device, you have to do:
- In Bluetooth & other devices settings, select Send or receive files via Bluetooth > Receive files.

More information here: [Receive files over Bluetooth for Windows][receiving_files_via_windows]

## Used technologies/libraries

- Scanning codes using the library: [Code Scanner][code_scanner] which is licensed MIT License
- Simulating Android in a test environment using a library: [Robolectric][robolectric] which is license MIT License
- Connecting to an sshd server and using port forwarding, X11 forwarding, file transfer with the [JSch][jsch] library by [JCraft][jcraft] under the [BSD style license][bsd]

## Screenshots

The screenshots shown below are licensed [CC-BY-4.0][cc-by-4.0]

## License

![alt text][gnu_gplv3_logo]\
Scan Code WMS created as part of an internship for Inn-Tek Sp. z o.o.\
![alt text][inn_tek_logo]\
The ic_inn_tek_logo.png is the logo of Inn-Tek Sp. z o.o.\
[GNU GPLv3][gnu_gplv3_link] © Bartosz Gieras

[receiving_files_via_windows]: https://support.microsoft.com/en-us/windows/receive-files-over-bluetooth-d8da2667-e79b-744c-c135-f58af38fc3ba
[code_scanner]: https://github.com/yuriy-budiyev/code-scanner
[robolectric]: https://github.com/robolectric/robolectric
[jsch]: http://www.jcraft.com/jsch/
[jcraft]: http://www.jcraft.com/c-info.html
[bsd]: http://www.jcraft.com/jsch/LICENSE.txt
[smbj]: https://github.com/hierynomus/smbj
[gnu_gplv3_logo]: https://www.gnu.org/graphics/gplv3-127x51.png "GNU GPLv3"
[gnu_gplv3_link]: https://www.gnu.org/licenses/gpl-3.0.html
[inn_tek_logo]: https://inn-tek.com/images/headers/raindrops.jpg
