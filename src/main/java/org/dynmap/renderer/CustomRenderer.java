package org.dynmap.renderer;

import java.util.List;
import java.util.Map;

import org.dynmap.renderer.RenderPatchFactory.SideVisible;

/**
 * Abstract base class for custom renderers - used to allow creation of customized patch sets for blocks
 * 
 * Custom renderer classes are loaded by classname, and must have a default constructor
 */
public abstract class CustomRenderer {
    /**
     * Constructor - subclass must have public default constructor
     */
    protected CustomRenderer() {
        
    }
    /**
     * Initialize custom renderer
     * 
     * If overridden, super.initializeRenderer() should be called and cause exit if false is returned
     *
     * @param rpf - render patch factory (used for allocating patches)
     * @param blkid - block type ID
     * @param blockdatamask - block data mask (bit N=1 if block data value N is to be handled by renderer)
     * @param custparm - parameter strings for custom renderer - renderer specific
     * @return true if initialized successfully, false if not
     */
    public boolean initializeRenderer(RenderPatchFactory rpf, int blkid, int blockdatamask, Map<String,String> custparm) {
        return true;
    }
    /**
     * Cleanup custom renderer
     * 
     * If overridden, super.cleanupRenderer() should be called
     */
    public void cleanupRenderer() {
        
    }
    /**
     * Return list of fields from the TileEntity associated with the blocks initialized for the renderer, if any.
     * 
     * @return array of field ID strings, or null if none (the default)
     */
    public String[] getTileEntityFieldsNeeded() {
        return null;
    }
    /**
     * Return the maximum number of texture indices that may be returned by the renderer.  Used to validate
     * the teture mapping defined for the block definitions.
     * @return highest texture index that may be returned, plus 1.  Default is 1.
     */
    public int getMaximumTextureCount() {
        return 1;
    }
    /**
     * Return list of patches for block at given coordinates.  List will be treated as read-only, so the same list can
     * and should be returned, when possible, for different blocks with the same patch list.  The provided map data
     * context will always allow reading of data for the requested block, any data within its chunk, and any block
     * within one block in any direction of the requested block, at a minimum.  Also will include any requested tile
     * entity data for those blocks.
     * 
     * @param mapDataCtx - Map data context: can be used to read any data available for map.
     * @return patch list for given block
     */
    public abstract RenderPatch[] getRenderPatchList(MapDataContext mapDataCtx);

    private static final int[] default_patches = { 0, 0, 0, 0, 0, 0 };
    /**
     *  Utility method: add simple box to give list
     * @param rpf - patch factory
     * @param list - list to add patches to
     * @param xmin - minimum for X axis
     * @param xmax - maximum for X axis
     * @param ymin - minimum for Y axis
     * @param ymax - maximum for Y axis
     * @param zmin - minimum for Z axis
     * @param zmax - maximum for Z axis
     * @param patchids - patch IDs for each face (bottom,top,xmin,xmax,zmin,zmax)
     */
    protected void addBox(RenderPatchFactory rpf, List<RenderPatch> list, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, int[] patchids)  {
        if(patchids == null) {
            patchids = default_patches;
        }
        /* Add top */
        if(patchids[1] >= 0)
            list.add(rpf.getPatch(0, ymax, 1, 1, ymax, 1, 0, ymax, 0, xmin, xmax, 1-zmax, 1-zmin, SideVisible.TOP, patchids[1]));
        /* Add bottom */
        if(patchids[0] >= 0)
            list.add(rpf.getPatch(0, ymin, 1, 1, ymin, 1, 0, ymin, 0, xmin, xmax, 1-zmax, 1-zmin, SideVisible.TOP, patchids[0]));
        /* Add minX side */
        if(patchids[2] >= 0)
            list.add(rpf.getPatch(xmin, 0, 0, xmin, 0, 1, xmin, 1, 0, zmin, zmax, ymin, ymax, SideVisible.TOP, patchids[2]));
        /* Add maxX side */
        if(patchids[3] >= 0)
            list.add(rpf.getPatch(xmax, 0, 1, xmax, 0, 0, xmax, 1, 1, 1-zmax, 1-zmin, ymin, ymax, SideVisible.TOP, patchids[3]));
        /* Add minZ side */
        if(patchids[4] >= 0)
            list.add(rpf.getPatch(1, 0, zmin, 0, 0, zmin, 1, 1, zmin, 1-xmax, 1-xmin, ymin, ymax, SideVisible.TOP, patchids[4]));
        /* Add maxZ side */
        if(patchids[5] >= 0)
            list.add(rpf.getPatch(0, 0, zmax, 1, 0, zmax, 0, 1, zmax, xmin, xmax, ymin, ymax, SideVisible.TOP, patchids[5]));
    }

}
