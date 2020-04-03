package com.lib.anno

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FILE, AnnotationTarget.PROPERTY)
annotation class BindField(val value: Int = 0)
