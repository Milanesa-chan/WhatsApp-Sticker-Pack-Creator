# WhatsApp Sticker Pack Creator (WASPC)

A simple Java application to create sticker packs (for WhatsApp).

[Changelog](/CHANGELOG.md) - [Command line commands](/COMMANDS.md) - [License (Apache 2.0)](/LICENSE)

## Considerations

* This application can only be executed on Windows.
* You need to have the [android-sdk](https://developer.android.com/studio/?hl=es-419#downloads) installed and updated on your PC.
* Requires Java 8 to be installed on your PC.
* You must adhere to WhatsApp Terms and Conditions.
* Only produces ".apk" format files.
* Requires all images to be .jpg or .png in all of their variations.


## How-To

1. Download latest release from releases page (the zip file).
1. Extract the contents of the file in an isolated folder.
1. Put all your images to be used as stickers inside the "input" folder (packed with the app).
1. You must include one (and only one) image called "tray" of JPG or PNG format. This will be used as icon for the WhatsApp tab.
1. Execute "Start.bat" and enter your desired stickerpack name when asked.
1. When process is finished (could take several minutes) you should see an apk file inside the "output" folder.

For any issue that appears first check the console output, it's very informative and anyone who can read english can understand and fix most of the errors. If the issue seems greater ask the owner of this repository.


## Third-Party Software

This application relies on WhatsApp's [stickers](https://github.com/WhatsApp/stickers) software to create Android apk's.

[See license](/tools/third-party/WhatsApp-stickers/stickers-master/LICENSE)
