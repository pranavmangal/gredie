import * as React from "react";
import { useEffect, useState } from "react";

import { Box, CircularProgress, Typography } from "@mui/material";

const waitrose_url = "http://localhost:8080/getWaitrosePrice?items=";
const morrisons_url = "http://localhost:8080/getMorrisonsPrice?items=";

export default function Prices({ ingredients }) {
  const [waitrosePrice, setWaitrosePrice] = useState(0);
  const [waitroseItems, setWaitroseItems] = useState([]);

  const [morrisonsPrice, setMorrisonsPrice] = useState(0);
  const [morrisonsItems, setMorrisonsItems] = useState([]);

  useEffect(() => {
    fetch(waitrose_url + getIngredientNames())
      .then((res) => res.json())
      .then((data) => {
        setWaitrosePrice(parseFloat(data.totalPrice).toFixed(2));
        setWaitroseItems(data.itemList);
      });

    fetch(morrisons_url + getIngredientNames())
      .then((res) => res.json())
      .then((data) => {
        setMorrisonsPrice(parseFloat(data.totalPrice).toFixed(2));
        setMorrisonsItems(data.itemList);
      });

    fetch(
      `https://api.allorigins.win/get?url=${encodeURIComponent(
        "https://crossorigin.me/https://www.sainsburys.co.uk/groceries-api/gol-services/product/v1/product?filter[keyword]=tomato&page_number=1&page_size=60"
      )}`
    )
      .then((res) => res.json())
      .then((data) => console.log(data.products));
  }, [ingredients]);

  function getIngredientNames() {
    return ingredients.map((ingredient) => ingredient.name).join(",");
  }

  return (
    <Box>
      {waitrosePrice + morrisonsPrice !== 0 ? (
        <Box>
          <Typography variant="h5" style={{ marginBottom: 10 }}>
            Prices
          </Typography>

          <Box display="flex">
            {waitrosePrice !== 0 && (
              <Box style={{ marginRight: 80 }}>
                <Price name="Waitrose" price={waitrosePrice} />
                {waitroseItems.map((item, key) => (
                  <Box key={key}>
                    <Typography variant="h6">
                      <a href={item.url}>{item.name}</a> - £{item.price}
                    </Typography>
                  </Box>
                ))}
              </Box>
            )}

            {morrisonsPrice !== 0 && (
              <Box style={{ marginRight: 80 }}>
                <Price name="Morrisons" price={morrisonsPrice} />
                {morrisonsItems.map((item, key) => (
                  <Box key={key}>
                    <Typography variant="h6">
                      {item.name} - £{item.price}
                    </Typography>
                  </Box>
                ))}
              </Box>
            )}
          </Box>
        </Box>
      ) : (
        <Box>
          <CircularProgress style={{ marginBottom: 10 }} />
          <Typography variant="h6">Loading Prices</Typography>
        </Box>
      )}
    </Box>
  );
}

function Price({ name, price }) {
  return (
    <Box display="flex" flexDirection="column">
      <Typography variant="h5" style={{ marginBottom: 20 }}>
        {name}
      </Typography>
      <Typography variant="h4">£{price}</Typography>
    </Box>
  );
}
