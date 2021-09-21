// Configuration file
// ------------------
require.config({

    // Sets the js folder as the base directory for all futurerelative paths
    baseUrl: "resources/js",
    
    // Extra query string arguments appended to URLs that RequireJS uses to fetch resources. Most useful to cache bust
    // Change this whenever any update is done to any javascript file
    urlArgs: "version=1.0.0",

    // 3rd party script alias names
    paths: {

        // Core Libraries
        // --------------

        // jQuery
        "jquery": "vendor/jquery.min",

        // Plugins
        // -------

        // Twitter Bootstrap jQuery plugins
        "bootstrap": "vendor/bootstrap.min",
        // Form Validator
        "jquery.validate": "vendor/jquery.validate.min",
        "additional-methods": "vendor/additional-methods.min",
        // Cascading Dropdown
    	"jquery.cascadingdropdown": "vendor/jquery.cascadingdropdown",
    	// Multi-Column Select
    	"Multi-Column-Select": "vendor/Multi-Column-Select",
        // Table Tree
    	"jquery.treetable": "vendor/jquery.treetable",    	
    	// Count Down Plugin
    	"TimeCircles": "vendor/TimeCircles",
    	// jQuery DataTables Plugin
    	"jquery.dataTables": "vendor/jquery.dataTables.min",
    	"dataTables.bootstrap": "vendor/dataTables.bootstrap",
    	// jBox JS files
    	"jBox": "vendor/jBox.min",
    	// Bootstrap Checkbox to Toggle Plugin JS
    	"bootstrap-toggle": "vendor/bootstrap-toggle.min",
    	// Bootstrap In place edditing
    	"bootstrap-editable": "vendor/bootstrap-editable.min",
    	// Easing Plugin
    	"jquery.easing": "vendor/jquery.easing.min",
    	//Simple Side Bar
    	"simpler-sidebar": "vendor/simpler-sidebar.min",
    	// Forala WYSWIG Editor
    	"froala_editor":"vendor/froala_editor.min",
    	"froala_tables":"vendor/froala-plugins/tables.min",
    	"froala_lists":"vendor/froala-plugins/lists.min",
    	"froala_colors":"vendor/froala-plugins/colors.min",
    	"froala_font_family":"vendor/froala-plugins/font_family.min",
    	"froala_font_size":"vendor/froala-plugins/font_size.min",
    	"froala_block_styles":"vendor/froala-plugins/block_styles.min",
    	"froala_media_manager":"vendor/froala-plugins/media_manager.min",
    	"froala_video":"vendor/froala-plugins/video.min",
    	"froala_inline_styles":"vendor/froala-plugins/inline_styles.min",
    	"froala_fullscreen":"vendor/froala-plugins/fullscreen.min",
    	"froala_char_counter":"vendor/froala-plugins/char_counter.min",
    	"froala_entities":"vendor/froala-plugins/entities.min",
    	"froala_file_upload":"vendor/froala-plugins/file_upload.min",
    	"froala_urls":"vendor/froala-plugins/urls.min"
    },

    // Sets the configuration for your scripts that are notAMD compatible
    shim: {

        // Twitter Bootstrap jQuery plugins depend on jQuery
        "bootstrap": ["jquery"],
	    // Form Validator plugins depend on jQuery
	    "jquery.validate": ["jquery"],
	    "jquery.cascadingdropdown": ["jquery"],
	    "Multi-Column-Select": ["jquery"],
	    "jquery.treetable": ["jquery"],
	    "TimeCircles": ["jquery"],
	    "jquery.dataTables": ["jquery"],
	    "dataTables.bootstrap": ["jquery.dataTables","bootstrap"],
	    "jBox": {
	    	deps : ["jquery"],
	    	exports : "jBox"
	    },
	    "bootstrap-toggle": ["bootstrap"],
	    "bootstrap-editable": ["bootstrap"],
	    "jquery.easing": ["jquery"],
	    "simpler-sidebar": ["jquery.easing"],
	    "froala_editor": ["jquery"],
	    "froala_tables": ["froala_editor"],
	    "froala_lists": ["froala_editor"],
	    "froala_colors": ["froala_editor"],
	    "froala_font_family": ["froala_editor"],
	    "froala_font_size": ["froala_editor"],
	    "froala_block_styles": ["froala_editor"],
	    "froala_media_manager": ["froala_editor"],
	    "froala_video": ["froala_editor"],
	    "froala_inline_styles": ["froala_editor"],
	    "froala_fullscreen": ["froala_editor"],
	    "froala_char_counter": ["froala_editor"],
	    "froala_entities": ["froala_editor"],
	    "froala_file_upload": ["froala_editor"],
	    "froala_urls": ["froala_editor"]
    }
});