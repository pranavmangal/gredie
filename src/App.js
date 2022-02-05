import * as React from "react";

import { useEffect, useState } from "react";

import Container from "@mui/material/Container";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";

export default function App() {
  const [url, setUrl] = useState("");
  const [ingredients, setIngredients] = useState([]);

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    setUrl(params.get("url"));
  }, []);

  useEffect(() => {
    fetch(`/api?url=${url}`)
      .then((res) => res.json())
      .then((data) => setIngredients(data));
  }, [url]);

  return (
    <Container>
      <Box>
        <Typography>Ingredients Found:</Typography>
        {ingredients.map((ingredient) => (
          <Typography>{ingredient.line}</Typography>
        ))}
      </Box>
    </Container>
  );
}
