package net.gir.girsignals.models;

import java.util.HashMap;
import java.util.function.Consumer;

import net.gir.girsignals.GirsignalsMain;
import net.gir.girsignals.blocks.SignalHV;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GIRCustomModelLoader implements ICustomModelLoader {
	
	private static HashMap<String, Consumer<SignalCustomModel>> registeredModels = new HashMap<>(); 
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		registeredModels.clear();
		registeredModels.put("hvsignal", cm -> {
			cm.register("hv_base", ebs -> true, 0);
			cm.register("hv_mast_sign", ebs -> true, 1);
			cm.register("hv_mast_number", ebs -> true, 2);
			cm.register("hv_zs3v", ebs -> true, 3);
			cm.register("hv_vr", ebs -> true, 4);
			cm.register("hv_zs1", ebs -> ebs.getValue(SignalHV.ZS1) != null, 5f);
			cm.register("hv_zs7", ebs -> true, 4.6f);
			cm.register("hv_hp", ebs -> {
				System.out.println(ebs.toString());
				return ebs.getValue(SignalHV.HAUPTSIGNAL) != null;
			}, 5.4f);
			cm.register("hv_zs3", ebs -> true, 6.9f);
		});
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		if(!modelLocation.getResourceDomain().equals(GirsignalsMain.MODID)) return false;
		return registeredModels.containsKey(modelLocation.getResourcePath());
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		ModelResourceLocation mrl = (ModelResourceLocation) modelLocation;
		return new SignalCustomModel(registeredModels.get(modelLocation.getResourcePath()), EnumFacing.byName(mrl.getVariant().split("=")[1]));
	}

}