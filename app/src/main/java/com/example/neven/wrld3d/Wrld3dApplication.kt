package com.example.neven.wrld3d

import android.app.Application
import com.eegeo.mapapi.EegeoApi

class Wrld3dApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        EegeoApi.init(this, ConstantsWrld3d.WRLD3D_API_KEY)
    }
}