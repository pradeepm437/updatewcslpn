module.exports = function (grunt) {

	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),
		project_name: '<%= pkg.name.replace(/-/g, "_") %>', // name of your app's chef role
		archive_name: '<%= pkg.name %>-<%= pkg.version %>.zip',	// file name `play dist`. Can be found in Build.scala or build.sbt
		artifact_name: 'current.zip',		// `current.zip` by default. (optional)
		release: {
			options: {
				commit: false,
				tag: false,
				push: false,
				pushTags: false,
				npm: false
			}
		},
		redmart_builder: {
			options: {
				// clean: { },		// if anything needs to be cleaned before compression
				// compress: { },   // if anything needs to be compressed
				//triggerChef: false,  // true be default, uncomment to turn off auto deployment
				archivePath: '../target/universal/' // path to your 'archive' file (optional)
			}
		}
	});

	grunt.loadNpmTasks('semvar-ghflow-grunt-redmart-builder');
	grunt.loadNpmTasks('grunt-release');
	grunt.registerTask('default', 'redmart_builder');
};
