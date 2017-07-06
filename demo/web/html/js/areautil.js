/**
 * Created by HeartZeus on 2017/2/24.
 */
define(['/web/html/js/city.data-3_new.js'], function (city) {
    var getProvince = function() {
        return formatData(city.cityData);
    };

    var formatData = function(areaData) {
        var data = [];
        for (var i = 0; i < areaData.length; i ++) {
            data[i] = {
                value: areaData[i].value,
                text: areaData[i].text
            }
        }
        return data;
    };

    var getChildren = function (areaData, childId) {
        for (var i = 0; i < areaData.length; i ++) {
            if (childId === areaData[i].value) {
                return areaData[i].children;
            }
        }
    };

    var getAreaName = function (areaData, areaId) {
        for (var i = 0; i < areaData.length; i ++) {
            if (areaId === areaData[i].value) {
                return areaData[i].text;
            }
        }
    };


    var getCity = function(provinceId) {
        var data = getChildren(city.cityData, provinceId);
        return formatData(data);
    };

    var getCountry = function (provinceId, cityId) {
        var cityData = getChildren(city.cityData, provinceId);
        var countryData = getChildren(cityData, cityId);
        return formatData(countryData);
    };

    var getFullAddress = function (provinceId, cityId, countryId, detail_address) {
        if (provinceId === "" || provinceId === undefined) {
            return "--";
        }

        if (cityId === "" || cityId === undefined) {
            return "--";
        }

        if (countryId === "" || countryId === undefined) {
            return "--";
        }

        var provinceList = getProvince();
        var provinceName = getAreaName(provinceList, provinceId);

        var cityList = getCity(provinceId);
        var cityName = getAreaName(cityList, cityId);

        var countryList = getCountry(provinceId, cityId);
        var countryName = getAreaName(countryList, countryId);

        var detailAddress = detail_address;

        return provinceName + cityName + countryName + detailAddress;
    };

    return {
        getProvince: getProvince,
        getCity: getCity,
        getCountry: getCountry,
        getAreaName: getAreaName,
        getFullAddress: getFullAddress,
    };
});