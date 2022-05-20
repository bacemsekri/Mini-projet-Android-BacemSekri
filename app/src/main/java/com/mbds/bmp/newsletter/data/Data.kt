package com.mbds.bmp.newsletter.data

import android.content.Context
import com.mbds.bmp.newsletter.R
import com.mbds.bmp.newsletter.model.Category
import com.mbds.bmp.newsletter.model.Country
import com.mbds.bmp.newsletter.model.Editor
import com.neovisionaries.i18n.CountryCode

class Data {
    companion object {
        fun getCategoryList() = listOf<Category>(
            Category(
                R.string.all,
                null,
                "https://cdn.pixabay.com/photo/2019/05/04/19/30/newspapers-4178905_960_720.jpg"
            ),
            Category(
                R.string.business,
                "business",
                "https://www.investigaction.net/wp-content/uploads/2020/10/area-line-charts-md.jpg"
            ),
            Category(
                R.string.entertainment,
                "entertainment",
                "https://cdn.pixabay.com/photo/2016/12/14/12/09/violin-1906127_960_720.jpg"
            ),
            Category(
                R.string.general,
                "general",
                "https://libreshot.com/wp-content/uploads/2016/02/demonstration-861x529.jpg"
            ),
            Category(
                R.string.health,
                "health",
                "https://etaples-sur-mer.fr/wp-content/uploads/2020/03/coronavirus-4914026_960_720.jpg"
            ),
            Category(
                R.string.sciences,
                "science",
                "https://cdn.pixabay.com/photo/2016/02/06/09/56/science-1182713_1280.jpg"
            ),
            Category(
                R.string.sports,
                "sports",
                "https://pixnio.com/free-images/2017/09/08/2017-09-08-06-04-51-1100x733.jpg"
            ),
            Category(
                R.string.technology,
                "technology",
                "https://pixnio.com/free-images/2018/06/29/2018-06-29-22-07-36-1200x800.jpg"
            )
        )

        fun getCountryList() = listOf<Country>(
            Country(null),
            Country(CountryCode.AE),
            Country(CountryCode.AR),
            Country(CountryCode.AT),
            Country(CountryCode.AU),
            Country(CountryCode.BE),
            Country(CountryCode.BG),
            Country(CountryCode.BR),
            Country(CountryCode.CA),
            Country(CountryCode.CH),
            Country(CountryCode.CN),
            Country(CountryCode.CO),
            Country(CountryCode.CU),
            Country(CountryCode.CZ),
            Country(CountryCode.DE),
            Country(CountryCode.EG),
            Country(CountryCode.FR),
            Country(CountryCode.GB),
            Country(CountryCode.GR),
            Country(CountryCode.HK),
            Country(CountryCode.HU),
            Country(CountryCode.ID),
            Country(CountryCode.IE),
            Country(CountryCode.IL),
            Country(CountryCode.IN),
            Country(CountryCode.IT),
            Country(CountryCode.JP),
            Country(CountryCode.KR),
            Country(CountryCode.LT),
            Country(CountryCode.LV),
            Country(CountryCode.MA),
            Country(CountryCode.MX),
            Country(CountryCode.MY),
            Country(CountryCode.NG),
            Country(CountryCode.NL),
            Country(CountryCode.NO),
            Country(CountryCode.PH),
            Country(CountryCode.PL),
            Country(CountryCode.PT),
            Country(CountryCode.RO),
            Country(CountryCode.RS),
            Country(CountryCode.RU),
            Country(CountryCode.SA),
            Country(CountryCode.SE),
            Country(CountryCode.SG),
            Country(CountryCode.SI),
            Country(CountryCode.SK),
            Country(CountryCode.TH),
            Country(CountryCode.TR),
            Country(CountryCode.TW),
            Country(CountryCode.UA),
            Country(CountryCode.US),
            Country(CountryCode.VE),
            Country(CountryCode.ZA)
        )

        fun getEditorForAll(context: Context) =
            Editor(null, context.getString(R.string.all), "", "", "", "", "")
    }
}