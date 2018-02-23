package com.example.georgia.sps_app1;


public class SensorsTable {

    private String _tmst;
    private  String LocalTime;
    private String _deviceId;
    private String _modelName;
    private int _key;
    private String _accVal0;
    private String _accVal1;
    private String _accVal2;
    private String _SSID;
    private String _RSSI;

    //Class constructor
    public SensorsTable(){
    }

    //Methods to set and get table variables


    public void setLocalTime(String localTime) {
        LocalTime = localTime;
    }

    public void set_tmst(String _tmst) {
        this._tmst = _tmst;
    }

    public void set_deviceId(String _deviceId) {
        this._deviceId = _deviceId;
    }

    public void set_modelName(String _modelName) {
        this._modelName = _modelName;
    }

    public void set_key(int _key) {
        this._key = _key;
    }

    public void set_accVal0(String _accVal0) {
        this._accVal0 = _accVal0;
    }

    public void set_accVal1(String _accVal1) {
        this._accVal1 = _accVal1;
    }

    public void set_accVal2(String _accVal2) {
        this._accVal2 = _accVal2;
    }

    public void set_SSID(String _SSID) {
        this._SSID = _SSID;
    }

    public void set_RSSI(String _RSSI) {
        this._RSSI = _RSSI;
    }

    public String getLocalTime() {
        return LocalTime;
    }

    public String get_tmst() {
        return _tmst;
    }

    public String get_deviceId() {
        return _deviceId;
    }

    public String get_modelName() {
        return _modelName;
    }

    public int get_key() {
        return _key;
    }

    public String get_accVal0() {
        return _accVal0;
    }

    public String get_accVal1() {
        return _accVal1;
    }

    public String get_accVal2() {
        return _accVal2;
    }

    public String get_SSID() {
        return _SSID;
    }

    public String get_RSSI() {
        return _RSSI;
    }
}
