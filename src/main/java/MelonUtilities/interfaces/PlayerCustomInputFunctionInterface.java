package MelonUtilities.interfaces;

public interface PlayerCustomInputFunctionInterface {

	CustomInput melonutilities$getCustomInputFunction();

	void melonutilities$setCustomInputFunction(CustomInput customInput);

	@FunctionalInterface
	public static interface CustomInput{
		void apply(String chatMessage);
	}

}
