/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import utilities.UtilitiesLibrary

object EmailSerializer: KSerializer<UtilitiesLibrary.Values.Email> {
    override val descriptor: SerialDescriptor
        get() = String.serializer().descriptor

    override fun deserialize(decoder: Decoder): UtilitiesLibrary.Values.Email {
        return UtilitiesLibrary.Values.Email(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: UtilitiesLibrary.Values.Email) {
        encoder.encodeString(value.email)
    }
}