package com.xiaoxianben.lazymystical.tileEntity;

import net.minecraft.util.IIntArray;

public class IntArraySeedCultivator implements IIntArray {

    protected final int[] ints;


    public IntArraySeedCultivator(int size) {
        this(new int[size]);
    }

    public IntArraySeedCultivator(int... ints) {
        this.ints = ints;
    }


    public int get(int index) {
        return this.ints[index];
    }

    public void set(int index, int value) {
        this.ints[index] = value;
    }

    public int getCount() {
        return this.ints.length;
    }

    public void modify(int index, int value) {
        this.ints[index] += value;
    }


    public int[] toIntArray() {
        return ints;
    }

}
