import * as React from "react";

import { Box, Typography } from "@mui/material";

export default function Ingredients({ ingredients }) {
  return (
    <Box>
      <Typography>Ingredients Found:</Typography>

      {ingredients.map((ingredient, key) => (
        <Typography key={key}>{ingredient.line}</Typography>
      ))}
    </Box>
  );
}
