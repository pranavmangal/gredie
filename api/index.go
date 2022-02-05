package handler

import (
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/schollz/ingredients"
)

func Handler(w http.ResponseWriter, r *http.Request) {
	// Allow CORS
	w.Header().Set("Access-Control-Allow-Origin", "*")

	url := r.URL.Query().Get("url")
	recipe, _ := ingredients.NewFromURL(url)
	ingredients := recipe.IngredientList()

	b, _ := json.Marshal(ingredients)
	fmt.Fprintf(w, string(b))
}
