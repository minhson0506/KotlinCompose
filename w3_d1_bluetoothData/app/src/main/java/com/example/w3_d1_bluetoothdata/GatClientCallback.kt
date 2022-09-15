package com.example.w3_d1_bluetoothdata

import android.bluetooth.*
import android.util.Log
import java.util.*

class GattClientCallback() : BluetoothGattCallback() {
    val TAG = "w3_d1_bluetoothdata"
    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        if (status == BluetoothGatt.GATT_FAILURE) {
            Log.d(TAG, "GATT connection failure")
            //...
            return
        } else if (status == BluetoothGatt.GATT_SUCCESS) {
            Log.d(TAG, "GATT connection success")
            //return
        }
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Log.d(TAG, "Connected GATT service") //...
            gatt.discoverServices();
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            Log.d(TAG, "onConnectionStateChange: disconnect")
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        super.onServicesDiscovered(gatt, status)
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return
        }
        Log.d(TAG, "onServicesDiscovered()")
        for (gattService in gatt.services) {
            Log.d(TAG, "Service ${gattService.uuid}")
//            if (gattService.uuid == UUID.fromString("00001801-0000-1000-8000-00805f9b34fb")) {
                if (gattService.uuid == UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb")) {
                Log.d(TAG, "BINGO!!!")
                for (gattCharacteristic in gattService.characteristics)
                    Log.d("DBG",
                        "Characteristic ${gattCharacteristic.uuid} and name is ${gattCharacteristic.describeContents()} and ${gattCharacteristic.properties}")
                /* setup the system for the notification messages */
                val characteristic =
                    gatt.getService(UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb"))
//                    .getCharacteristic(convertFromInteger(0x2A37))
//                    .getCharacteristic(UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb"))
//                        .getCharacteristic(UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb"))
                    .getCharacteristic(UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb"))
                gatt.readCharacteristic(characteristic)
                if (gatt.setCharacteristicNotification(characteristic, true)) {
// then enable them on the server
                    Log.d(TAG, "onServicesDiscovered: start to write noti")
                    for (des in characteristic.descriptors)
                        Log.d(TAG,
                            "onServicesDiscovered: description is ${des.uuid} permission${des.permissions} value ${des.value} ")
                    Log.d(TAG, "onServicesDiscovered: finish des")
                    val descriptor =
//                        characteristic.getDescriptor(UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb"))
//                        characteristic.getDescriptor(UUID.fromString("00002800-0000-1000-8000-00805f9b34fb"))
                    characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
//                        characteristic.getDescriptor(convertFromInteger(0x2902))
                    descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
//                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                    if (gatt.writeDescriptor(descriptor)) {
                        Log.d(TAG, "onServicesDiscovered: finish to write")
                        Log.d(TAG,
                            "Characteristic ${characteristic.uuid} data received with value ${
                                characteristic.value
//                                characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16,
//                                    1)
                            } " +
//                                    "and value ${characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 1)} " +
                                    "with permission ${characteristic.permissions}")
                    }
                } else Log.d(TAG, "onServicesDiscovered: fail to write noti")
            }
//            for (gattCharacteristic in gattService.characteristics)
//                Log.d(TAG,
//                    "onServicesDiscovered: characteristic is ${gattCharacteristic.uuid} with permission ${gattCharacteristic.permissions}")
        }

//        val charac = gatt.getService(convertFromInteger(0x180D)).getCharacteristic()
//        val service =
//            gatt.getService(UUID.fromString("3A25EE4A-0D11-4156-B74F-DDD76B2508D4"))
////        Log.d(TAG, "onServicesDiscovered: value: ${service.characteristics}")
//                var characteristic = service.getCharacteristic(convertFromInteger(0x2A37))
//        Log.d(TAG, "onServicesDiscovered: value charac: ${characteristic.value}")
//        Log.d(TAG, "onServicesDiscovered value 1: ${onCharacteristicRead(gatt, characteristic, status)} ${characteristic.value}")
//        val characteristic2 =
//            gatt.getService(UUID.fromString("00001801-0000-1000-8000-00805f9b34fb"))
//                .getCharacteristic(UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb"))
//        Log.d(TAG, "onServicesDiscovered value 2: ${onCharacteristicRead(gatt, characteristic2, status)} ${characteristic2.value}")
//        val characteristic3 =
//            gatt.getService(UUID.fromString("00001801-0000-1000-8000-00805f9b34fb"))
//                .getCharacteristic(UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb"))
//        Log.d(TAG, "onServicesDiscovered value 3: ${onCharacteristicRead(gatt, characteristic3, status)} ${characteristic3.value}")
    }

    private fun convertFromInteger(i: Int): UUID {
        val MSB = 0x0000000000001000L
        val LSB = -0x7fffff7fa064cb05L
        val value = (i and -0x1).toLong()
        return UUID(MSB or (value shl 32), LSB)
    }

    override fun onDescriptorWrite(
        gatt: BluetoothGatt,
        descriptor: BluetoothGattDescriptor,
        status: Int,
    ) {
        super.onDescriptorWrite(gatt, descriptor, status)
        Log.d(TAG, "onDescriptorWrite status is $status and descriptor is ${descriptor.value}  ${descriptor.permissions}")
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
    ) {
        Log.d(TAG,
            "Characteristic data received with value ${
                characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16,
                    1)
            }")
    }
}