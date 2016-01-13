package com.nineoldandroids.animation;

import android.util.Log;
import com.nineoldandroids.util.FloatProperty;
import com.nineoldandroids.util.IntProperty;
import com.nineoldandroids.util.Property;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class PropertyValuesHolder
  implements Cloneable
{
  private static Class[] DOUBLE_VARIANTS;
  private static Class[] FLOAT_VARIANTS;
  private static Class[] INTEGER_VARIANTS;
  private static final TypeEvaluator sFloatEvaluator;
  private static final HashMap<Class, HashMap<String, Method>> sGetterPropertyMap;
  private static final TypeEvaluator sIntEvaluator = new IntEvaluator();
  private static final HashMap<Class, HashMap<String, Method>> sSetterPropertyMap;
  private Object mAnimatedValue;
  private TypeEvaluator mEvaluator;
  private Method mGetter = null;
  KeyframeSet mKeyframeSet = null;
  protected Property mProperty;
  final ReentrantReadWriteLock mPropertyMapLock = new ReentrantReadWriteLock();
  String mPropertyName;
  Method mSetter = null;
  final Object[] mTmpValueArray = new Object[1];
  Class mValueType;

  static
  {
    sFloatEvaluator = new FloatEvaluator();
    FLOAT_VARIANTS = new Class[] { Float.TYPE, Float.class, Double.TYPE, Integer.TYPE, Double.class, Integer.class };
    INTEGER_VARIANTS = new Class[] { Integer.TYPE, Integer.class, Float.TYPE, Double.TYPE, Float.class, Double.class };
    DOUBLE_VARIANTS = new Class[] { Double.TYPE, Double.class, Float.TYPE, Integer.TYPE, Float.class, Integer.class };
    sSetterPropertyMap = new HashMap();
    sGetterPropertyMap = new HashMap();
  }

  private PropertyValuesHolder(Property paramProperty)
  {
    this.mProperty = paramProperty;
    if (paramProperty != null)
      this.mPropertyName = paramProperty.getName();
  }

  private PropertyValuesHolder(String paramString)
  {
    this.mPropertyName = paramString;
  }

  static String getMethodName(String paramString1, String paramString2)
  {
    if ((paramString2 == null) || (paramString2.length() == 0))
      return paramString1;
    char c = Character.toUpperCase(paramString2.charAt(0));
    paramString2 = paramString2.substring(1);
    return paramString1 + c + paramString2;
  }

  private Method getPropertyFunction(Class paramClass1, String paramString, Class paramClass2)
  {
    Method localMethod1 = null;
    Class[] arrayOfClass = null;
    String str = getMethodName(paramString, this.mPropertyName);
    if (paramClass2 == null);
    while (true)
    {
      try
      {
        paramClass2 = paramClass1.getMethod(str, null);
        return paramClass2;
      }
      catch (NoSuchMethodException paramString)
      {
        paramClass2 = arrayOfClass;
        try
        {
          paramClass1 = paramClass1.getDeclaredMethod(str, null);
          paramClass2 = paramClass1;
          paramClass1.setAccessible(true);
          paramClass2 = paramClass1;
        }
        catch (NoSuchMethodException paramClass1)
        {
          Log.e("PropertyValuesHolder", "Couldn't find no-arg method for property " + this.mPropertyName + ": " + paramString);
        }
        continue;
      }
      arrayOfClass = new Class[1];
      int j;
      int i;
      if (this.mValueType.equals(Float.class))
      {
        paramString = FLOAT_VARIANTS;
        j = paramString.length;
        i = 0;
        paramClass2 = localMethod1;
      }
      while (true)
        while (true)
        {
          if (i >= j)
            break label265;
          Object localObject = paramString[i];
          arrayOfClass[0] = localObject;
          try
          {
            localMethod1 = paramClass1.getMethod(str, arrayOfClass);
            paramClass2 = localMethod1;
            this.mValueType = localObject;
            return localMethod1;
            if (this.mValueType.equals(Integer.class))
            {
              paramString = INTEGER_VARIANTS;
              break;
            }
            if (this.mValueType.equals(Double.class))
            {
              paramString = DOUBLE_VARIANTS;
              break;
            }
            paramString = new Class[1];
            paramString[0] = this.mValueType;
          }
          catch (NoSuchMethodException localNoSuchMethodException2)
          {
            try
            {
              Method localMethod2 = paramClass1.getDeclaredMethod(str, arrayOfClass);
              paramClass2 = localMethod2;
              localMethod2.setAccessible(true);
              paramClass2 = localMethod2;
              this.mValueType = localObject;
              return localMethod2;
            }
            catch (NoSuchMethodException localNoSuchMethodException2)
            {
              i += 1;
            }
          }
        }
      label265: Log.e("PropertyValuesHolder", "Couldn't find setter/getter for property " + this.mPropertyName + " with value type " + this.mValueType);
    }
  }

  public static PropertyValuesHolder ofFloat(Property<?, Float> paramProperty, float[] paramArrayOfFloat)
  {
    return new FloatPropertyValuesHolder(paramProperty, paramArrayOfFloat);
  }

  public static PropertyValuesHolder ofFloat(String paramString, float[] paramArrayOfFloat)
  {
    return new FloatPropertyValuesHolder(paramString, paramArrayOfFloat);
  }

  public static PropertyValuesHolder ofInt(Property<?, Integer> paramProperty, int[] paramArrayOfInt)
  {
    return new IntPropertyValuesHolder(paramProperty, paramArrayOfInt);
  }

  public static PropertyValuesHolder ofInt(String paramString, int[] paramArrayOfInt)
  {
    return new IntPropertyValuesHolder(paramString, paramArrayOfInt);
  }

  public static PropertyValuesHolder ofKeyframe(Property paramProperty, Keyframe[] paramArrayOfKeyframe)
  {
    KeyframeSet localKeyframeSet = KeyframeSet.ofKeyframe(paramArrayOfKeyframe);
    if ((localKeyframeSet instanceof IntKeyframeSet))
      return new IntPropertyValuesHolder(paramProperty, (IntKeyframeSet)localKeyframeSet);
    if ((localKeyframeSet instanceof FloatKeyframeSet))
      return new FloatPropertyValuesHolder(paramProperty, (FloatKeyframeSet)localKeyframeSet);
    paramProperty = new PropertyValuesHolder(paramProperty);
    paramProperty.mKeyframeSet = localKeyframeSet;
    paramProperty.mValueType = paramArrayOfKeyframe[0].getType();
    return paramProperty;
  }

  public static PropertyValuesHolder ofKeyframe(String paramString, Keyframe[] paramArrayOfKeyframe)
  {
    KeyframeSet localKeyframeSet = KeyframeSet.ofKeyframe(paramArrayOfKeyframe);
    if ((localKeyframeSet instanceof IntKeyframeSet))
      return new IntPropertyValuesHolder(paramString, (IntKeyframeSet)localKeyframeSet);
    if ((localKeyframeSet instanceof FloatKeyframeSet))
      return new FloatPropertyValuesHolder(paramString, (FloatKeyframeSet)localKeyframeSet);
    paramString = new PropertyValuesHolder(paramString);
    paramString.mKeyframeSet = localKeyframeSet;
    paramString.mValueType = paramArrayOfKeyframe[0].getType();
    return paramString;
  }

  public static <V> PropertyValuesHolder ofObject(Property paramProperty, TypeEvaluator<V> paramTypeEvaluator, V[] paramArrayOfV)
  {
    paramProperty = new PropertyValuesHolder(paramProperty);
    paramProperty.setObjectValues(paramArrayOfV);
    paramProperty.setEvaluator(paramTypeEvaluator);
    return paramProperty;
  }

  public static PropertyValuesHolder ofObject(String paramString, TypeEvaluator paramTypeEvaluator, Object[] paramArrayOfObject)
  {
    paramString = new PropertyValuesHolder(paramString);
    paramString.setObjectValues(paramArrayOfObject);
    paramString.setEvaluator(paramTypeEvaluator);
    return paramString;
  }

  private void setupGetter(Class paramClass)
  {
    this.mGetter = setupSetterOrGetter(paramClass, sGetterPropertyMap, "get", null);
  }

  private Method setupSetterOrGetter(Class paramClass1, HashMap<Class, HashMap<String, Method>> paramHashMap, String paramString, Class paramClass2)
  {
    Method localMethod1 = null;
    try
    {
      this.mPropertyMapLock.writeLock().lock();
      HashMap localHashMap = (HashMap)paramHashMap.get(paramClass1);
      if (localHashMap != null)
        localMethod1 = (Method)localHashMap.get(this.mPropertyName);
      Method localMethod2 = localMethod1;
      if (localMethod1 == null)
      {
        localMethod2 = getPropertyFunction(paramClass1, paramString, paramClass2);
        paramString = localHashMap;
        if (localHashMap == null)
        {
          paramString = new HashMap();
          paramHashMap.put(paramClass1, paramString);
        }
        paramString.put(this.mPropertyName, localMethod2);
      }
      return localMethod2;
    }
    finally
    {
      this.mPropertyMapLock.writeLock().unlock();
    }
    throw paramClass1;
  }

  private void setupValue(Object paramObject, Keyframe paramKeyframe)
  {
    if (this.mProperty != null)
      paramKeyframe.setValue(this.mProperty.get(paramObject));
    try
    {
      if (this.mGetter == null)
        setupGetter(paramObject.getClass());
      paramKeyframe.setValue(this.mGetter.invoke(paramObject, new Object[0]));
      return;
    }
    catch (InvocationTargetException paramObject)
    {
      Log.e("PropertyValuesHolder", paramObject.toString());
      return;
    }
    catch (IllegalAccessException paramObject)
    {
      Log.e("PropertyValuesHolder", paramObject.toString());
    }
  }

  void calculateValue(float paramFloat)
  {
    this.mAnimatedValue = this.mKeyframeSet.getValue(paramFloat);
  }

  public PropertyValuesHolder clone()
  {
    try
    {
      PropertyValuesHolder localPropertyValuesHolder = (PropertyValuesHolder)super.clone();
      localPropertyValuesHolder.mPropertyName = this.mPropertyName;
      localPropertyValuesHolder.mProperty = this.mProperty;
      localPropertyValuesHolder.mKeyframeSet = this.mKeyframeSet.clone();
      localPropertyValuesHolder.mEvaluator = this.mEvaluator;
      return localPropertyValuesHolder;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
    }
    return null;
  }

  Object getAnimatedValue()
  {
    return this.mAnimatedValue;
  }

  public String getPropertyName()
  {
    return this.mPropertyName;
  }

  void init()
  {
    TypeEvaluator localTypeEvaluator;
    if (this.mEvaluator == null)
    {
      if (this.mValueType != Integer.class)
        break label44;
      localTypeEvaluator = sIntEvaluator;
    }
    while (true)
    {
      this.mEvaluator = localTypeEvaluator;
      if (this.mEvaluator != null)
        this.mKeyframeSet.setEvaluator(this.mEvaluator);
      return;
      label44: if (this.mValueType == Float.class)
      {
        localTypeEvaluator = sFloatEvaluator;
        continue;
      }
      localTypeEvaluator = null;
    }
  }

  void setAnimatedValue(Object paramObject)
  {
    if (this.mProperty != null)
      this.mProperty.set(paramObject, getAnimatedValue());
    if (this.mSetter != null);
    try
    {
      this.mTmpValueArray[0] = getAnimatedValue();
      this.mSetter.invoke(paramObject, this.mTmpValueArray);
      return;
    }
    catch (InvocationTargetException paramObject)
    {
      Log.e("PropertyValuesHolder", paramObject.toString());
      return;
    }
    catch (IllegalAccessException paramObject)
    {
      Log.e("PropertyValuesHolder", paramObject.toString());
    }
  }

  public void setEvaluator(TypeEvaluator paramTypeEvaluator)
  {
    this.mEvaluator = paramTypeEvaluator;
    this.mKeyframeSet.setEvaluator(paramTypeEvaluator);
  }

  public void setFloatValues(float[] paramArrayOfFloat)
  {
    this.mValueType = Float.TYPE;
    this.mKeyframeSet = KeyframeSet.ofFloat(paramArrayOfFloat);
  }

  public void setIntValues(int[] paramArrayOfInt)
  {
    this.mValueType = Integer.TYPE;
    this.mKeyframeSet = KeyframeSet.ofInt(paramArrayOfInt);
  }

  public void setKeyframes(Keyframe[] paramArrayOfKeyframe)
  {
    int j = paramArrayOfKeyframe.length;
    Keyframe[] arrayOfKeyframe = new Keyframe[Math.max(j, 2)];
    this.mValueType = paramArrayOfKeyframe[0].getType();
    int i = 0;
    while (i < j)
    {
      arrayOfKeyframe[i] = paramArrayOfKeyframe[i];
      i += 1;
    }
    this.mKeyframeSet = new KeyframeSet(arrayOfKeyframe);
  }

  public void setObjectValues(Object[] paramArrayOfObject)
  {
    this.mValueType = paramArrayOfObject[0].getClass();
    this.mKeyframeSet = KeyframeSet.ofObject(paramArrayOfObject);
  }

  public void setProperty(Property paramProperty)
  {
    this.mProperty = paramProperty;
  }

  public void setPropertyName(String paramString)
  {
    this.mPropertyName = paramString;
  }

  void setupEndValue(Object paramObject)
  {
    setupValue(paramObject, (Keyframe)this.mKeyframeSet.mKeyframes.get(this.mKeyframeSet.mKeyframes.size() - 1));
  }

  void setupSetter(Class paramClass)
  {
    this.mSetter = setupSetterOrGetter(paramClass, sSetterPropertyMap, "set", this.mValueType);
  }

  void setupSetterAndGetter(Object paramObject)
  {
    Object localObject;
    if (this.mProperty != null)
    {
      try
      {
        this.mProperty.get(paramObject);
        Iterator localIterator = this.mKeyframeSet.mKeyframes.iterator();
        while (localIterator.hasNext())
        {
          localObject = (Keyframe)localIterator.next();
          if (((Keyframe)localObject).hasValue())
            continue;
          ((Keyframe)localObject).setValue(this.mProperty.get(paramObject));
        }
      }
      catch (ClassCastException localClassCastException)
      {
        Log.e("PropertyValuesHolder", "No such property (" + this.mProperty.getName() + ") on target object " + paramObject + ". Trying reflection instead");
        this.mProperty = null;
      }
    }
    else
    {
      Class localClass = paramObject.getClass();
      if (this.mSetter == null)
        setupSetter(localClass);
      localObject = this.mKeyframeSet.mKeyframes.iterator();
      while (((Iterator)localObject).hasNext())
      {
        Keyframe localKeyframe = (Keyframe)((Iterator)localObject).next();
        if (localKeyframe.hasValue())
          continue;
        if (this.mGetter == null)
          setupGetter(localClass);
        try
        {
          localKeyframe.setValue(this.mGetter.invoke(paramObject, new Object[0]));
        }
        catch (InvocationTargetException localInvocationTargetException)
        {
          Log.e("PropertyValuesHolder", localInvocationTargetException.toString());
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          Log.e("PropertyValuesHolder", localIllegalAccessException.toString());
        }
      }
    }
  }

  void setupStartValue(Object paramObject)
  {
    setupValue(paramObject, (Keyframe)this.mKeyframeSet.mKeyframes.get(0));
  }

  public String toString()
  {
    return this.mPropertyName + ": " + this.mKeyframeSet.toString();
  }

  static class FloatPropertyValuesHolder extends PropertyValuesHolder
  {
    float mFloatAnimatedValue;
    FloatKeyframeSet mFloatKeyframeSet;
    private FloatProperty mFloatProperty;

    public FloatPropertyValuesHolder(Property paramProperty, FloatKeyframeSet paramFloatKeyframeSet)
    {
      super(null);
      this.mValueType = Float.TYPE;
      this.mKeyframeSet = paramFloatKeyframeSet;
      this.mFloatKeyframeSet = ((FloatKeyframeSet)this.mKeyframeSet);
      if ((paramProperty instanceof FloatProperty))
        this.mFloatProperty = ((FloatProperty)this.mProperty);
    }

    public FloatPropertyValuesHolder(Property paramProperty, float[] paramArrayOfFloat)
    {
      super(null);
      setFloatValues(paramArrayOfFloat);
      if ((paramProperty instanceof FloatProperty))
        this.mFloatProperty = ((FloatProperty)this.mProperty);
    }

    public FloatPropertyValuesHolder(String paramString, FloatKeyframeSet paramFloatKeyframeSet)
    {
      super(null);
      this.mValueType = Float.TYPE;
      this.mKeyframeSet = paramFloatKeyframeSet;
      this.mFloatKeyframeSet = ((FloatKeyframeSet)this.mKeyframeSet);
    }

    public FloatPropertyValuesHolder(String paramString, float[] paramArrayOfFloat)
    {
      super(null);
      setFloatValues(paramArrayOfFloat);
    }

    void calculateValue(float paramFloat)
    {
      this.mFloatAnimatedValue = this.mFloatKeyframeSet.getFloatValue(paramFloat);
    }

    public FloatPropertyValuesHolder clone()
    {
      FloatPropertyValuesHolder localFloatPropertyValuesHolder = (FloatPropertyValuesHolder)super.clone();
      localFloatPropertyValuesHolder.mFloatKeyframeSet = ((FloatKeyframeSet)localFloatPropertyValuesHolder.mKeyframeSet);
      return localFloatPropertyValuesHolder;
    }

    Object getAnimatedValue()
    {
      return Float.valueOf(this.mFloatAnimatedValue);
    }

    void setAnimatedValue(Object paramObject)
    {
      if (this.mFloatProperty != null)
        this.mFloatProperty.setValue(paramObject, this.mFloatAnimatedValue);
      do
      {
        return;
        if (this.mProperty == null)
          continue;
        this.mProperty.set(paramObject, Float.valueOf(this.mFloatAnimatedValue));
        return;
      }
      while (this.mSetter == null);
      try
      {
        this.mTmpValueArray[0] = Float.valueOf(this.mFloatAnimatedValue);
        this.mSetter.invoke(paramObject, this.mTmpValueArray);
        return;
      }
      catch (InvocationTargetException paramObject)
      {
        Log.e("PropertyValuesHolder", paramObject.toString());
        return;
      }
      catch (IllegalAccessException paramObject)
      {
        Log.e("PropertyValuesHolder", paramObject.toString());
      }
    }

    public void setFloatValues(float[] paramArrayOfFloat)
    {
      super.setFloatValues(paramArrayOfFloat);
      this.mFloatKeyframeSet = ((FloatKeyframeSet)this.mKeyframeSet);
    }

    void setupSetter(Class paramClass)
    {
      if (this.mProperty != null)
        return;
      super.setupSetter(paramClass);
    }
  }

  static class IntPropertyValuesHolder extends PropertyValuesHolder
  {
    int mIntAnimatedValue;
    IntKeyframeSet mIntKeyframeSet;
    private IntProperty mIntProperty;

    public IntPropertyValuesHolder(Property paramProperty, IntKeyframeSet paramIntKeyframeSet)
    {
      super(null);
      this.mValueType = Integer.TYPE;
      this.mKeyframeSet = paramIntKeyframeSet;
      this.mIntKeyframeSet = ((IntKeyframeSet)this.mKeyframeSet);
      if ((paramProperty instanceof IntProperty))
        this.mIntProperty = ((IntProperty)this.mProperty);
    }

    public IntPropertyValuesHolder(Property paramProperty, int[] paramArrayOfInt)
    {
      super(null);
      setIntValues(paramArrayOfInt);
      if ((paramProperty instanceof IntProperty))
        this.mIntProperty = ((IntProperty)this.mProperty);
    }

    public IntPropertyValuesHolder(String paramString, IntKeyframeSet paramIntKeyframeSet)
    {
      super(null);
      this.mValueType = Integer.TYPE;
      this.mKeyframeSet = paramIntKeyframeSet;
      this.mIntKeyframeSet = ((IntKeyframeSet)this.mKeyframeSet);
    }

    public IntPropertyValuesHolder(String paramString, int[] paramArrayOfInt)
    {
      super(null);
      setIntValues(paramArrayOfInt);
    }

    void calculateValue(float paramFloat)
    {
      this.mIntAnimatedValue = this.mIntKeyframeSet.getIntValue(paramFloat);
    }

    public IntPropertyValuesHolder clone()
    {
      IntPropertyValuesHolder localIntPropertyValuesHolder = (IntPropertyValuesHolder)super.clone();
      localIntPropertyValuesHolder.mIntKeyframeSet = ((IntKeyframeSet)localIntPropertyValuesHolder.mKeyframeSet);
      return localIntPropertyValuesHolder;
    }

    Object getAnimatedValue()
    {
      return Integer.valueOf(this.mIntAnimatedValue);
    }

    void setAnimatedValue(Object paramObject)
    {
      if (this.mIntProperty != null)
        this.mIntProperty.setValue(paramObject, this.mIntAnimatedValue);
      do
      {
        return;
        if (this.mProperty == null)
          continue;
        this.mProperty.set(paramObject, Integer.valueOf(this.mIntAnimatedValue));
        return;
      }
      while (this.mSetter == null);
      try
      {
        this.mTmpValueArray[0] = Integer.valueOf(this.mIntAnimatedValue);
        this.mSetter.invoke(paramObject, this.mTmpValueArray);
        return;
      }
      catch (InvocationTargetException paramObject)
      {
        Log.e("PropertyValuesHolder", paramObject.toString());
        return;
      }
      catch (IllegalAccessException paramObject)
      {
        Log.e("PropertyValuesHolder", paramObject.toString());
      }
    }

    public void setIntValues(int[] paramArrayOfInt)
    {
      super.setIntValues(paramArrayOfInt);
      this.mIntKeyframeSet = ((IntKeyframeSet)this.mKeyframeSet);
    }

    void setupSetter(Class paramClass)
    {
      if (this.mProperty != null)
        return;
      super.setupSetter(paramClass);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.nineoldandroids.animation.PropertyValuesHolder
 * JD-Core Version:    0.6.0
 */