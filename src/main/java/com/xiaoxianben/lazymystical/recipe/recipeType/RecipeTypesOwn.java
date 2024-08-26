package com.xiaoxianben.lazymystical.recipe.recipeType;

import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public class RecipeTypesOwn {

    //    public static final IRecipeType<NullType> recipe_null = new IRecipeType<NullType>() {
//        @Override
//        public Class<NullType> getClassType() {
//            return NullType.class;
//        }
//
//        @Override
//        public JsonObject getRecipeJson(NullType nullType) {
//            JsonObject json = new JsonObject();
//
//            json.addProperty("name", "null");
//
//            return json;
//        }
//
//        @Override
//        public NullType getRecipe(JsonObject json) {
//            return null;
//        }
//    };
    public static final IRecipeType<ItemStack> recipe_itemStack = new IRecipeType<ItemStack>() {
        @Override
        public Class<ItemStack> getClassType() {
            return ItemStack.class;
        }

        @Override
        public JsonObject getRecipeJson(ItemStack itemStack) {
            JsonObject json = new JsonObject();

            String name = Objects.requireNonNull(itemStack.getItem().getRegistryName()).toString();
            int count = itemStack.getCount();

            json.addProperty("name", name);
            json.addProperty("count", count);

            return json;
        }

        @Override
        public ItemStack getRecipe(JsonObject json) {
            return new ItemStack(Objects.requireNonNull(Registry.ITEM.get(new ResourceLocation(json.get("name").getAsString()))), json.get("count").getAsInt());
        }
    };
    public static final IRecipeType<Item> recipe_item = new IRecipeType<Item>() {
        @Override
        public Class<Item> getClassType() {
            return Item.class;
        }

        @Override
        public JsonObject getRecipeJson(Item item) {
            JsonObject json = new JsonObject();

            String name = Objects.requireNonNull(item.getRegistryName()).toString();

            json.addProperty("name", name);

            return json;
        }

        @Override
        public Item getRecipe(JsonObject json) {
            return Objects.requireNonNull(Registry.ITEM.get(new ResourceLocation(json.get("name").getAsString())));
        }
    };
    public static final IRecipeType<FluidStack> recipe_fluidStack = new IRecipeType<FluidStack>() {
        @Override
        public Class<FluidStack> getClassType() {
            return FluidStack.class;
        }

        @Override
        public JsonObject getRecipeJson(FluidStack fluidStack) {
            JsonObject json = new JsonObject();

            String name = Objects.requireNonNull(fluidStack.getFluid().getRegistryName()).toString();

            json.addProperty("name", name);
            json.addProperty("count", fluidStack.getAmount());

            return json;
        }

        @Override
        public FluidStack getRecipe(JsonObject json) {
            return new FluidStack(Registry.FLUID.get(new ResourceLocation(json.get("name").getAsString())), json.get("count").getAsInt());
        }
    };
    public static final IRecipeType<Fluid> recipe_fluid = new IRecipeType<Fluid>() {
        @Override
        public Class<Fluid> getClassType() {
            return Fluid.class;
        }

        @Override
        public JsonObject getRecipeJson(Fluid fluid) {
            JsonObject json = new JsonObject();

            String name = Objects.requireNonNull(fluid.getRegistryName()).toString();

            json.addProperty("name", name);

            return json;
        }

        @Override
        public Fluid getRecipe(JsonObject json) {
            return Registry.FLUID.get(new ResourceLocation(json.get("name").getAsString()));
        }
    };
    //    public static final IRecipeType<ItemOrFluid> recipe_itemOrFluid = new IRecipeType<ItemOrFluid>() {
//        @Override
//        public Class<ItemOrFluid> getClassType() {
//            return ItemOrFluid.class;
//        }
//
//        @Override
//        public JsonObject getRecipeJson(ItemOrFluid itemOrFluid) {
//            JsonObject json;
//
//            if (itemOrFluid.get() instanceof ItemStack) {
//                json = recipe_itemStack.getRecipeJson((ItemStack) itemOrFluid.get());
//            } else {
//                json = recipe_fluidStack.getRecipeJson((FluidStack) itemOrFluid.get());
//            }
//
//            return json;
//        }
//
//        @Override
//        public ItemOrFluid getRecipe(JsonObject json) {
//            return new ItemOrFluid(json.get("name").getAsString(), json.get("count").getAsInt());
//        }
//    };
//    public static final IRecipeType<Integer> recipe_energy = new RecipeInt("energy");
//    public static final IRecipeType<Integer> recipe_level = new RecipeInt("level");
    public static final IRecipeType<Float> recipe_float = new RecipeFloat("multiplier");

}
