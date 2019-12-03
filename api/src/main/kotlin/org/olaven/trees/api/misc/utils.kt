package org.olaven.trees.api.misc

import java.time.ZonedDateTime

fun epochMilli() =
        ZonedDateTime.now()
                .toInstant()
                .toEpochMilli()