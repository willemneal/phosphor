package edu.columbia.cs.psl.phosphor.runtime;


import edu.columbia.cs.psl.phosphor.runtime.Taint;
import edu.columbia.cs.psl.phosphor.struct.ControlTaintTagStack;
import edu.columbia.cs.psl.phosphor.struct.LazyArrayIntTags;
import edu.columbia.cs.psl.phosphor.struct.LazyArrayObjTags;
import edu.columbia.cs.psl.phosphor.struct.LazyCharArrayIntTags;
import edu.columbia.cs.psl.phosphor.struct.LazyCharArrayObjTags;
import edu.columbia.cs.psl.phosphor.struct.TaintedWithIntTag;
import edu.columbia.cs.psl.phosphor.struct.TaintedWithObjTag;

public class MyTaintChecker extends TaintChecker{
	private static String name() {
		String methodName = Thread.currentThread().getStackTrace()[callStackDepth].getMethodName();
		return Thread.currentThread().getStackTrace()[callStackDepth].getClassName() +"."
				   + methodName.substring(0, methodName.length() - 16);
	}
	private static int callStackDepth = 3;
	public static void checkTaint(int tag)
	{
					
			if(tag != 0)
			System.out.println(name());
	}

	public static void checkTaint(Taint tag)
	{
		
		if(tag != null)
			System.out.println(name());
	}

	public static void checkTaint(Object obj) {
		
		if(obj == null)
			return;
		if (obj instanceof TaintedWithIntTag) {
			if (((TaintedWithIntTag) obj).getPHOSPHOR_TAG() != 0)
				System.out.println(name());
		}
		else if (obj instanceof TaintedWithObjTag) {
			if (((TaintedWithObjTag) obj).getPHOSPHOR_TAG() != null)
				System.out.println(name());
		}

		else if(obj instanceof int[])
		{
			for(int i : ((int[])obj))
			{
				if(i > 0)
					System.out.println(name());
			}
		}
		else if(obj instanceof LazyArrayIntTags)
		{
			LazyArrayIntTags tags = ((LazyArrayIntTags) obj);
			if (tags.taints != null)
				for (int i : tags.taints) {
					if (i > 0)
						System.out.println(name());
				}
		}
		else if(obj instanceof LazyArrayObjTags)
		{
			LazyArrayObjTags tags = ((LazyArrayObjTags) obj);
			if (tags.taints != null)
				for (Object i : tags.taints) {
					if (i != null)
						System.out.println(name());
				}
		}
		else if(obj instanceof Object[])
		{
			for(Object o : ((Object[]) obj))
				checkTaint(o);
		}
		else if(obj instanceof ControlTaintTagStack)
		{
			ControlTaintTagStack ctrl = (ControlTaintTagStack) obj;
			if(ctrl.taint != null && !ctrl.isEmpty())
			{
				System.out.println(name());
			}
		}
		else if(obj instanceof Taint)
		{
			System.out.println(name());
		}
	}

	public static boolean hasTaints(int[] tags) {
		if(tags == null)
			return false;
		for (int i : tags) {
			if (i != 0)
				return true;
		}
		return false;
	}
	public static void setTaints(Object obj, int tag) {
		if(obj == null)
			return;
		if (obj instanceof TaintedWithIntTag) {
			((TaintedWithIntTag) obj).setPHOSPHOR_TAG(tag);
		} else if (obj instanceof LazyArrayIntTags){
			((LazyArrayIntTags)obj).setTaints(tag);
		} else if (obj.getClass().isArray()) {
			
				Object[] ar = (Object[]) obj;
				for (Object o : ar)
					setTaints(o, tag);
			
		}
		if(obj instanceof Iterable)
		{
			for(Object o : ((Iterable)obj))
				setTaints(o, tag);
		}
	}
	public static void setTaints(LazyCharArrayObjTags tags, Object tag) {
		if(tags.val.length == 0)
			return;
		tags.taints = new Taint[tags.val.length];
		for (int i = 0; i < tags.val.length; i++)
			tags.taints[i] = (Taint) tag;
	}
	public static void setTaints(Object obj, Taint tag) {
		if(obj == null)
			return;
		if (obj instanceof TaintedWithObjTag) {
			((TaintedWithObjTag) obj).setPHOSPHOR_TAG(tag);
		} else if (obj instanceof LazyArrayObjTags){
			((LazyArrayObjTags)obj).setTaints(tag);
		} else if (obj.getClass().isArray()) {
			
				Object[] ar = (Object[]) obj;
				for (Object o : ar)
					setTaints(o, tag);
			
		}
		if(obj instanceof Iterable)
		{
			for(Object o : ((Iterable)obj))
				setTaints(o, tag);
		}
	}

	public static void setTaints(LazyCharArrayIntTags tags, int tag) {
		if(tags.val.length == 0)
			return;
		tags.taints = new int[tags.val.length];
		for (int i = 0; i < tags.val.length; i++)
			tags.taints[i] = tag;
	}
	public static void setTaints(Taint[] array, Taint tag) {
		if(array == null)
			return;
		for (int i = 0; i < array.length; i++)
			array[i] = tag;
	}
}

