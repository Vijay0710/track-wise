package com.eyeshield.sharewise.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ClientSocketModule {

    /**
     * This is a special ip for an emulator running in the host machine..
     * We port forward the client and server running in emulator to host machine since
     * one emulator cannot directly communicate with the other
     *
     * Below is the command to port forward the client and server running in emulator to host machine
     *
     * adb -s emulator-5554 forward tcp:8080 tcp:8080
     * adb -s emulator-5556 forward tcp:8080 tcp:8080
     *
     * After port forwarding we use 10.0.2.2 (For emulators) in client machine to connect to the
     * socket server running in the emulator (which is also port forwarded in client machine)
     *
     * The below IP is only for testing purposes, in real world identify the ip of the host android
     * device
     *
     * TODO: To get the IP address dynamically from the connected network
     * **/
    @Provides
    fun providesServerIp(): String = "10.0.2.2"

}