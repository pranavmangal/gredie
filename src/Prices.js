import * as React from "react";
import { useEffect, useState } from "react";

import { Box, CircularProgress, Typography } from "@mui/material";

const waitrose_url = "http://localhost:8080/getWaitrosePrice?items=";
const morrisons_url = "http://localhost:8080/getMorrisonsPrice?items=";

export default function Prices({ ingredients }) {
  const [waitrosePrice, setWaitrosePrice] = useState(0);
  const [morrisonsPrice, setMorrisonsPrice] = useState(0);

  useEffect(() => {
    fetch(waitrose_url + getIngredientNames())
      .then((res) => res.text())
      .then((data) => setWaitrosePrice(data));

    fetch(morrisons_url + getIngredientNames())
      .then((res) => res.text())
      .then((data) => setMorrisonsPrice(data));
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

          <Box display="flex" alignItems="center">
            {waitrosePrice !== 0 && (
              <Price name="Waitrose" price={waitrosePrice} />
            )}

            {morrisonsPrice !== 0 && (
              <Price name="Morrisons" price={morrisonsPrice} />
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
    <Box display="flex" flexDirection="column" style={{ marginRight: 40 }}>
      <Typography variant="h5" style={{ marginBottom: 20 }}>
        {name}
      </Typography>
      <Typography variant="h4">£ {price}</Typography>
    </Box>
  );
}
