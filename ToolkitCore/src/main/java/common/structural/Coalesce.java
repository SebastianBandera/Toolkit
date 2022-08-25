package common.structural;

import java.util.function.Supplier;

public class Coalesce {

	/**
	 * Returns the first not-null if any. Uses suppliers to avoid evaluate all the arguments in all cases.
	 * @param <T>
	 * @param suppliers
	 * @return First not-null if any
	 */
	@SafeVarargs
	public static <T> T coalesceWithSuppliers(Supplier<T> ... suppliers) {
	    if (suppliers == null) {
			return null;
		}
		for(Supplier<T> supplier : suppliers) {
			if (supplier != null) {
				T dataFromSupplier = supplier.get();
				if(dataFromSupplier != null) return dataFromSupplier;
			}
	    }
	    return null;
	}
}
