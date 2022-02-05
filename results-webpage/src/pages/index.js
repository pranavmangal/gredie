import * as React from "react"
import { useQueryParam, ArrayParam } from 'use-query-params';
// markup
const IndexPage = () => {

  const [ingredients = [], setIngredients] = useQueryParam('ingredients', ArrayParam);

  if(ingredients.length === 0) {
    return <h1>No Results</h1>
  }

  const parsedIngredients = ingredients.map((ingredient) => JSON.parse(ingredient));

  return (
    parsedIngredients.map((parsedIngredient)=> <h1>{parsedIngredient.name}</h1>)
  );
}

export default IndexPage
