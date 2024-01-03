package com.khadas.util;

import static androidx.core.math.MathUtils.clamp;

import static java.lang.Math.abs;

public class MathHelper {


    private static final float SMALL_NUMBER =0.000001f;

    /** For the given Value clamped to the [Input:Range] inclusive, returns the corresponding percentage in [Output:Range] Inclusive. */
    static   public float MapValueToRange(float InMin, float InMax, float OutMin, float OutMax, float Value)
    {
        float ClampedPct = clamp(GetRangePct(InMin,InMax, Value), 0.0f, 1.0f);
        return GetRangeValue(OutMin,OutMax, ClampedPct);
    }

    static public  float GetRangeValue(float RangeMin, float RangeMax, float Pct)
    {
        return lerp(RangeMin, RangeMax, Pct);
    }
    public static float lerp(float start, float stop, float amount) {
        return (1 - amount) * start + amount * stop;
    }
    static public float GetRangePct(float MinValue, float MaxValue, float Value)
    {
        // Avoid Divide by Zero.
        // But also if our range is a point, output whether Value is before or after.
        float Divisor = MaxValue - MinValue;
        if (IsNearlyZero(Divisor))
        {
            return (Value >= MaxValue) ? 1.0f : 0.0f;
        }

        return (Value - MinValue) / Divisor;
    }

    static public boolean IsNearlyZero(float Value)
    {
        return abs(Value) <= SMALL_NUMBER;
    }
}
