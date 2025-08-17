package com.xiaoxianben.lazymystical.jsonRecipe.recipeType;

import com.google.gson.JsonObject;

@SuppressWarnings("unused")
public interface IRecipeType<T> {

    Class<T> getClassType();

    JsonObject getRecipeJson(T recipe);

    T getRecipe(JsonObject json);

    default boolean equals(T o1, T o2) {
        return o1.equals(o2);
    }

}
