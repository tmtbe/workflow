package com.thoughtworks

import test.Base64
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.Security
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec

class Run {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val SIGNATURE_ALGORITHM = "SHA256withRSA"
            val appBffPrivateKey =
                    """
                        MIIEpAIBAAKCAQEAxXtnv7W6k44w12+hPdFQ1Tg0s8TmSpqftyn2NxM/NPL0eDxz
                        +twU440lJyWC3B1vzH1WpXw/l+vSjtKcQV1UWAlKVrdPrsIDJiuh7NEXfDHdd/JP
                        nYqHIZsjWM9pMUQPv2atq1tT02grj5CRBq2xAL5zShhcOIAgi+yGcOIdoO1IHJpE
                        BHaZnHuMm6wVDy48d46/0euNJCWIsomqe4ltj5/s/lPzpUPSA9doJtQmzEaK56y1
                        jlMGkX5vkteNwtSib2wG8neh3q2iezU0PvNsXomHiksANadPyXN3YqIHalBMIQt/
                        iXfo+tfqbSPEAK4OC4A934jqcpywXy76K/RAAQIDAQABAoIBAHJF5ZQesZceyY5w
                        bwpZa//lpUF9J5F2BgO0SWA3R/T4vZQxzx8TaRb/nscXQrDia8YVpSTfT+E2OcIh
                        hv+f5OWIgrbJHlfOtonI58skdwWLyU7WLVHJTkaZAgwrAQDcFIy5+rbUgx4rAkHx
                        8qm9qlX23rm6fOrI4Yx2UplRjrmGcAi5tHnRS8fGJuud2Pco7L1J4cNwhUILWqbt
                        toxKlR21hOhH3SEMCqe6trR6NsOqHwsBA0todoKGBvw3FKgNzML9eRvIaCVojrgE
                        VyUfwULkw8SMneYWu339J9cFYq8EB7Uo9qjVo9nLRsag2zScck5sL7pdBWIioB8H
                        s8GGJQECgYEA7ODBYUvCSBxjjHjQznsSUKHH/xOwkAOVCCy5xJj2riU9ld3oEh3l
                        axuJDcF52K5KOHz3l8/thJYZpVebh6rzSMb9pTVD4pMtGMZMY629kp2IMh2gIHy1
                        kon6gXYhG/W8jadB11Et1m047RQDqlkKesSdHph1Q0PEQIZ81O5l4VECgYEA1WyB
                        uLdbFgA0oa18n2pUsZxB+7oam8tcrHCPGnAVYYwvu/lZ1z+TaLmR2UAamwiL998J
                        8POBJtRuzebrhlNErbr3biGc1v9MJywVLZRbVWmPDLZmnBBfbmkVyHwiBd4RxzPv
                        gHJ6EGWYqtSWkPQhp8P/flyc1QSZw2nagGQmR7ECgYEAzM6nZNBz8pVZHSN4po6S
                        j1TR7yG/D0WJnb/N/sWiw38/eEydUsy2h50PKu2VxFstoRmrmX8WBgUht2u/9dVA
                        9DPZKvaa81P9MeaCNc0Kk7HRCEPFJ1GsTD8wcK8KgXo+xNtQejKeRz+4cINVh/vk
                        q7FDiCiZesck8AVX+WMGg4ECgYBYJQ9Sdc/Vv1H9cERnJqhCDpIl4A/7RjtJMNe5
                        iI8cEaTpUqurPaOo/fNhsZsLarU8M07MyBUQL25v9a5SdPOJeQ2V26YGGbye9RLu
                        Fza9iPI+wtqQHCb918LaYYI/wB7Bkg6Zky4ctYkfDNv+lAWOX2GXE7LmO5cx4PcQ
                        TcyNkQKBgQCNX8fZb+PRF9/qZKxH31kAY3dx1rxI+7Ufx5l1aAzwSt6RIi9HRIyE
                        zkMOqnq+Eo+gxOfP/Ot3DpMHCeBG8Vh35PrQpT7l2yiaxZEC+/Ee5tv6ooTpAFSH
                        iPhg3YZnECF/wMSChe7loxFMLNZ6MDihpquPaRHuU+QoDV2wcppdQQ==
                    """.trimIndent()

            val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
            fun getPickupApiPrivateKey(secret: String): PrivateKey {
                val keyString = secret.replace("\\n", "")
                // TODO More accurate conditions
                val kf = KeyFactory.getInstance("RSA")
                val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.decode(keyString, Base64.NO_WRAP))
                return kf.generatePrivate(keySpecPKCS8)
            }
            signature.initSign(getPickupApiPrivateKey(appBffPrivateKey))
        }
    }
}