import * as React from "react";

import { useEffect, useState } from "react";

import Container from "@mui/material/Container";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import { CircularProgress } from "@mui/material";

import Ingredients from "./Ingredients";

const waitrose_url = "http://localhost:8080/getWaitrosePrice?items=";
const morrisons_url = "http://localhost:8080/getMorrisonsPrice?items=";

export default function App() {
  const [url, setUrl] = useState("");
  const [ingredients, setIngredients] = useState([]);
  const [waitrosePrice, setWaitrosePrice] = useState(0);
  const [morrisonsPrice, setMorrisonsPrice] = useState(0);

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    setUrl(params.get("url"));
  }, []);

  useEffect(() => {
    fetch(`/api?url=${url}`)
      .then((res) => res.json())
      .then((data) => setIngredients(data.ingredients));
  }, [url]);

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
    <Container>
      <Box>
        {Object.keys(ingredients).length !== 0 ? (
          <Box>
            <Ingredients ingredients={ingredients} />

            <Box style={{ padding: "40px" }} />

            <Typography>Prices</Typography>

            {waitrosePrice !== 0 && (
              <Typography>Waitrose: {waitrosePrice}</Typography>
            )}

            {morrisonsPrice !== 0 && (
              <Typography>Morrisons: {morrisonsPrice}</Typography>
            )}
          </Box>
        ) : (
          <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            flexDirection="column"
          >
            <CircularProgress />
            <Typography>Loading Ingredients</Typography>
          </Box>
        )}
      </Box>
    </Container>
  );
}
