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
    fetch(`https://autoshop-ic.vercel.app/api?url=${url}`)
      .then((res) => res.json())
      .then((data) => console.log(data));
  }, [url]);

  return (
    <Container>
      <Box>
        <Typography>{url}</Typography>
      </Box>
    </Container>
  );
}
