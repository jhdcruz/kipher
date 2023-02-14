package kipher.aes

/** AES encryption [mode]s. */
enum class AesModes(val mode: String) {
    /** `AES/GCM/NoPadding` */
    GCM("AES/GCM/NoPadding"),

    /** `AES/CBC/PKCS5Padding` */
    CBC("AES/CBC/PKCS5Padding"),

    /** `AES/CTR/NoPadding` */
    @Deprecated("Will be phased out, use the default GCM instead.", level = DeprecationLevel.WARNING)
    CTR("AES/CTR/NoPadding"),

    /** `AES/CFB/NoPadding` */
    @Deprecated("Will be phased out, use the default GCM instead.", level = DeprecationLevel.WARNING)
    CFB("AES/CFB/NoPadding"),

    /** `AES/OFB/NoPadding` */
    @Deprecated("Will be phased out, use the default GCM instead.", level = DeprecationLevel.WARNING)
    OFB("AES/OFB/NoPadding"),
}
