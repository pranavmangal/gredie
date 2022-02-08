# ic-hack-22

# How does it work
All the user needs to do is go a recipe webpage and click one button and Gredie will parse out the ingredients, scrape webpages from various supermarkets and give the total basket prices along with ingredients to buy.

# How we built it
The user facing element is Chrome extension that they can use on recipe webpages. This talks to a React page which calls a Go API to parse ingredients and a Java backend with multiple scrapers to fetch ingredients and prices from supermarket websites and display them to the user.

# What's next for Gredie
Connect to accounts for various supermarkets to add all the ingredients to your basket
