package kipher.aes

/** AES encryption [mode]s. */
enum class AesModes(val mode: String) {
    /** `AES/GCM/NoPadding` */
    GCM("AES/GCM/NoPadding"),

    /** `AES/CBC/PKCS5Padding` */
    CBC("AES/CBC/PKCS5Padding"),

    /** `AES/CTR/NoPadding` */
    CTR("AES/CTR/NoPadding"),

    /** `AES/CFB/NoPadding` */
    CFB("AES/CFB/NoPadding"),

    /** `AES/OFB/NoPadding` */
    OFB("AES/OFB/NoPadding");
}
