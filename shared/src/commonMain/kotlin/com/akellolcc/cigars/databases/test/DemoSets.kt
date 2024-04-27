/*******************************************************************************************************************************************
 * Copyright (C) 2024 Igor Kosulin
 * Last modified 4/27/24, 11:49 AM
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************************************************************************/

package com.akellolcc.cigars.databases.test

class DemoTestSets {
    companion object {
        val humidorsSet =
            """[{"rowid": -1,"name": "Second","brand": "Klaro","holds": 150,"count": 0,"temperature": "72","humidity": "72","notes": "","link": "","autoOpen": false,"sorting": 0,"type": 0,"price": 99.99,"other": 0 },{"rowid": -1,"name": "Case Elegance Renzo Humidor","brand": "Klaro","holds": 50,"count": 0,"temperature": "72","humidity": "72","notes": "This humidor is","link": "https://caseelegance.com/products/glass-top-cedar-humidor-with-front-digital-hygrometer","autoOpen": false,"sorting": 0,"type": 0,"price": 99.99,"other": 1 }]"""
        val cigarsSet =
            """[{"rowid": 0,"name": "#1 Fuente Fuente OpusX Reserva d’Chateau","brand": "Fabrica de Tabacos Raices Cubanas S. de R.L.","country": "Dominican","date": 1703980800000,"cigar": "Churchill","wrapper": "Dominican","binder": "Dominican","filler": "Dominican","gauge": 48,"length": "7'","strength": "MediumToFull","rating": 97,"myrating": 0,"notes": "“You’re not a cigarmaker.","link": "https://www.cigaraficionado.com/top25cigar/fuente-fuente-opusx-reserva-d-chateau-2023","favorites": false,"shopping": false,"count": 10,"price": 19.15,"other": 1 },{"rowid": 1,"name": "#2 Padrón Serie 1926 No. 48 Maduro","brand": "Padrón Cigars Inc.","country": "Nicaragua","date": 1703980800000,"cigar": "Robusto","wrapper": "Nicaragua","binder": "Nicaragua","filler": "Nicaragua","gauge": 60,"length": "5' 1/2“","strength": "Medium","rating": 96,"myrating": 1,"notes": "Say the word “Padrón” to most cigar smokers, and the response is usually universal. Their heads nod knowingly and their eyes light up. This predictable reaction is a testament to the company’s consistency year in and year out. Because all of Padrón’s tobaccos are grown in the open sunlight, they are bold and rich. Because the tobaccos are properly aged, they impart an elegance without being overpowering. Balance like this is uncommon and the reason why Padrón makes it to the Top 25 list every year. This cigar in particular celebrates two anniversaries. The brand came out in 2002 to celebrate founder José O. Padrón’s 75th birthday. In 2016, Padrón released the No. 48 size to celebrate the 48th anniversary of the Tobacconists’ Association of America. At first, the cigar was sold exclusively through TAA retailers. Today, it’s officially part of the Serie 1926 line. One might expect the thick 60 ring gauge to result in some dilution of the blend, but that didn’t happen. With its rich smoke of cocoa, almond, honey and cinnamon, the No. 48 is proof that Padrón could successfully scale up the blend and preserve all the desirable qualities that make the Serie 1926 so distinct.","link": "https://www.cigaraficionado.com/top25cigar/padron-serie-1926-no-48-maduro-2023","favorites": false,"shopping": false,"count": 10,"price": 24.90,"other": 1 }]"""
        val imagesSet = "[]"
    }
}
