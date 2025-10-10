package com.example.carrentalapp

import Car

object CarRepository {
    val availableCars = mutableListOf<Car>()
    val rentedCars = mutableListOf<Car>()
    val favourites = mutableListOf<Car>()

    var creditBalance = 500

    init {
        availableCars.add(
            Car(
                id = 0,
                name = "Volkswagen",
                model = "Cabriolet Orange Peel",
                year = 1974,
                rating = 4.6f,
                kilometres = 89000,
                dailyCost = 180,
                imageResId = R.drawable.car1,
                description = "This 1974 Volkswagen Cabriolet in a stunning Orange Peel finish is a classic symbol of open-air freedom. Perfect for scenic drives along the coast, its vintage charm and smooth handling make it an eye-catching rental for those who appreciate timeless European design."
            )
        )

        availableCars.add(
            Car(
                id = 1,
                name = "Plymouth",
                model = "340 Duster Bahama Yellow",
                year = 1971,
                rating = 4.8f,
                kilometres = 75000,
                dailyCost = 200,
                imageResId = R.drawable.car2,
                description = "The 1971 Plymouth 340 Duster in Bahama Yellow is a piece of American muscle history. Known for its powerful 340 V8 engine and sporty profile, this car delivers both performance and nostalgia, ideal for muscle car enthusiasts seeking an authentic retro driving experience."
            )
        )

        availableCars.add(
            Car(
                id = 2,
                name = "Chevrolet",
                model = "3100 Pickup",
                year = 1953,
                rating = 4.9f,
                kilometres = 64000,
                dailyCost = 220,
                imageResId = R.drawable.car3,
                description = "This 1953 Chevy 3100 Pickup in deep Burgundy brings vintage charm to modern roads. With its classic curved fenders and reliable inline-six engine, it’s perfect for those wanting to relive the golden era of American craftsmanship in comfort and style."
            )
        )

        availableCars.add(
            Car(
                id = 3,
                name = "Bentley",
                model = "Van Den Plas Tourer",
                year = 1927,
                rating = 4.7f,
                kilometres = 42000,
                dailyCost = 300,
                imageResId = R.drawable.car4,
                description = "The 1927 Bentley Van Den Plas Tourer is a true collector’s masterpiece. With its long bonnet, exposed wheels, and green bodywork, it offers a glimpse into pre-war luxury motoring — a rare opportunity to drive a piece of motoring history."
            )
        )

        availableCars.add(
            Car(
                id = 4,
                name = "Pontiac",
                model = "Star Chief Convertible",
                year = 1955,
                rating = 4.5f,
                kilometres = 59000,
                dailyCost = 210,
                imageResId = R.drawable.car5,
                description = "The 1955 Pontiac Star Chief Convertible in sky blue is the ultimate cruising companion. Its wide chrome grille, elegant lines, and smooth ride embody 1950s Americana — perfect for sunny weekend drives or nostalgic photo sessions."
            )
        )
    }
}
