package eu.gir.girsignals.guis;

import java.util.function.Consumer;

import eu.gir.girsignals.EnumSignals.IIntegerable;
import eu.gir.girsignals.guis.GuiPlacementtool.InternalUnlocalized;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GUISettingsSlider extends GuiButton implements InternalUnlocalized{

	protected IIntegerable<?> property;
	protected boolean dragging = false;
	protected int value = 0;
	protected int max;
	protected String buttonText;
	protected Consumer<Integer> consumer;
	protected String unlocalized;
		
	public GUISettingsSlider(IIntegerable<?> property, int buttonId, int x, int y, int width, String buttonText, int initialValue, Consumer<Integer> consumer) {
		super(buttonId, x, y, "");
		this.buttonText = I18n.format("property." + buttonText + ".name");
		this.displayString = this.buttonText + ": " + property.getObjFromID(initialValue).toString();
		this.property = property;
		this.value = initialValue;
		this.max = property.count() - 1;
		this.enabled = false;
		this.width = width;
		this.consumer = consumer;
		this.unlocalized = buttonText;
	}

	public int getValue() {
		return this.value;
	}
	
	private int getValue(int mouseX) {
		int val = Math.max(Math.min((int) (((mouseX - (this.x + 4)) / (float) (this.width - 8)) * (this.max + 1)), this.max), 0);
		consumer.accept(val);
		return val;
	}
	
	private String getValueString(int id) {
		return buttonText + ": " + property.getObjFromID(id).toString();
	}
	
	@Override
	protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			if (this.dragging) {
				this.value = getValue(mouseX);
	            this.displayString = getValueString(this.value);
			}

			GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES,
					this.x + (int) (this.value * (float) ((this.width - 8) / (this.max + 1))), this.y, 0, 66, 8, this.height, 200,
					20, 2, 3, 2, 2, this.zLevel);
		}
	}
	
	@Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible && this.hovered)
        {
			this.value = getValue(mouseX);
            this.displayString = getValueString(this.value);
            this.dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

	@Override
    public void mouseReleased(int mouseX, int mouseY)
    {
        this.dragging = false;
    }

	@Override
	public String getUnlocalized() {
		return this.unlocalized;
	}


}
