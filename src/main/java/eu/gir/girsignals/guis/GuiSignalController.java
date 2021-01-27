package eu.gir.girsignals.guis;

import java.util.ArrayList;
import java.util.function.Consumer;

import eu.gir.girsignals.EnumSignals.IIntegerable;
import eu.gir.girsignals.SEProperty;
import eu.gir.girsignals.blocks.SignalBlock;
import eu.gir.girsignals.tileentitys.SignalControllerTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiSignalController extends GuiScreen
		implements IIntegerable<GuiSignalController.Stages>, Consumer<Integer> {

	private GUISettingsSlider slider;
	private final SignalBlock block;
	private final SignalControllerTileEntity tile;
	
	public GuiSignalController(BlockPos pos, World world) {
		this.tile = (SignalControllerTileEntity) world.getTileEntity(pos);
		if(!this.tile.hasLinkImpl()) {
			this.block = null;
			return;
		}
		IBlockState state = world.getBlockState(this.tile.getLinkedPosition());
		this.block = (SignalBlock) state.getBlock();
		
	}

	public static enum Stages {
		MANUELL, REDSTONE, SCRIPTING
	}

	private Stages currentStage = Stages.MANUELL;

	@Override
	public void initGui() {
		this.slider = new GUISettingsSlider(this, -100, (this.width - 150) / 2, 10, 150,
				"stagetype", 0, this);
		
		if(block == null) {
			// TODO No link message
			System.out.println("NOBLOCK");
			return;
		}
		this.addButton(slider);
		switch (currentStage) {
		case MANUELL:
			initManuell();
			break;
		case REDSTONE:
			initRedstone();
			break;
		case SCRIPTING:
			initScripting();
			break;
		default:
			break;
		}
	}

	private void initManuell() {
		ArrayList<SEProperty<?>> availableProps = new ArrayList<>();

		int maxWidth = 0;

		for (int x : tile.getSupportedSignalTypesImpl()) {
			SEProperty<?> prop = (SEProperty<?>) block.getPropertyFromID(x);
			String format = I18n.format("property." + prop.getName() + ".name");
			availableProps.add((SEProperty<?>) prop);
			maxWidth = Math.max(fontRenderer.getStringWidth(format), maxWidth);
		}

		int y = 30;
		int x = 30;
		for (SEProperty<?> entry : availableProps) {
			this.addButton(new GUISettingsSlider(entry, 0, x, (y += 30), maxWidth, entry.getName(), 0, in -> {
			}));
			if (y >= 250) {
				y = 30;
				x += maxWidth + 20;
			}
		}
	}

	private void initRedstone() {
		// TODO Add redstone support
	}

	private void initScripting() {
		// TODO Add scripting
	}

	@Override
	public void onGuiClosed() {

	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void accept(Integer t) {
		currentStage = getObjFromID(t);
		this.buttonList.clear();
		initGui();
	}

	@Override
	public Stages getObjFromID(int obj) {
		return Stages.values()[obj];
	}

	@Override
	public int count() {
		return Stages.values().length;
	}

}