import * as React from "react";

import { useEffect, useState } from "react";

import Container from "@mui/material/Container";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import { AppBar, CircularProgress } from "@mui/material";

import Ingredients from "./Ingredients";
import Prices from "./Prices";

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
      .then((data) => setIngredients(data.ingredients));
  }, [url]);

  return (
    <Box>
      <AppBar position="static" style={{ alignItems: "center" }}>
        <Typography variant="h4" style={{ padding: 5 }}>
          gredi
        </Typography>
      </AppBar>
      <Box style={{ padding: "80px" }}>
        {Object.keys(ingredients).length !== 0 ? (
          <Box>
            <Ingredients ingredients={ingredients} />

            <Box style={{ padding: "40px" }} />

            <Prices ingredients={ingredients} />
          </Box>
        ) : (
          <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            flexDirection="column"
          >
            <CircularProgress style={{ marginBottom: 10 }} />
            <Typography variant="h6">Loading Ingredients</Typography>
          </Box>
        )}
      </Box>
    </Box>
  );
}
