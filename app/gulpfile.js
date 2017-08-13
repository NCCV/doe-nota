var gulp = require('gulp');
var gutil = require('gulp-util');
var bower = require('bower');
var concat = require('gulp-concat');
var sass = require('gulp-sass');
var minifyCss = require('gulp-minify-css');
var rename = require('gulp-rename');
var sh = require('shelljs');
var replace = require('gulp-replace-task');
var fs      = require('fs');

// Production
var templateCache = require('gulp-angular-templatecache');
var useref = require('gulp-useref');

var paths = {
  sass: ['./scss/**/*.scss'],
  templatecache: ['./www/templates/**/*.html'],
  useref: ['./www/*.html']
};

gulp.task('default', ['sass', 'templatecache', 'useref']);

gulp.task('sass', function(done) {
  gulp.src('./scss/ionic.app.scss')
    .pipe(sass())
    .on('error', sass.logError)
    .pipe(gulp.dest('./www/css/'))
    .pipe(minifyCss({
      keepSpecialComments: 0
    }))
    .pipe(rename({ extname: '.min.css' }))
    .pipe(gulp.dest('./www/css/'))
    .on('end', done);
});

gulp.task('watch', ['sass'], function() {
  gulp.watch(paths.sass, ['sass']);
  gulp.watch(paths.templatecache, ['templatecache']);
  gulp.watch(paths.useref, ['useref']);
});

gulp.task('install', ['git-check'], function() {
  return bower.commands.install()
    .on('log', function(data) {
      gutil.log('bower', gutil.colors.cyan(data.id), data.message);
    });
});

gulp.task('git-check', function(done) {
  if (!sh.which('git')) {
    console.log(
      '  ' + gutil.colors.red('Git is not installed.'),
      '\n  Git, the version control system, is required to download Ionic.',
      '\n  Download git here:', gutil.colors.cyan('http://git-scm.com/downloads') + '.',
      '\n  Once git is installed, run \'' + gutil.colors.cyan('gulp install') + '\' again.'
    );
    process.exit(1);
  }
  done();
});

gulp.task('replace', function () {
  // Get the environment from the command line
  var profile = gutil.env.profile || 'development';

  // Read the settings from the right file
  var filename = profile + '.json';
  var settings = JSON.parse(fs.readFileSync('./environment/profile/' + filename, 'utf8'));

// Replace each placeholder with the correct value for the variable.
  gulp.src('environment/profile/js/constants.js')
    .pipe(replace({
      patterns: [
        {match: 'couchUrl', replacement: settings.couchUrl},
        {match: 'profile', replacement: profile},
        {match: 'token', replacement: settings.token},
        {match: 'scanditApiKey', replacement: settings.scanditApiKey},
        {match: 'scanditCodeDuplicateFilter', replacement: settings.scanditCodeDuplicateFilter},
        {match: 'ocrUrl', replacement: settings.ocrUrl},
        {match: 'ocrKey', replacement: settings.ocrKey}
      ]
    }))
    .pipe(gulp.dest('www/js'));
});

// Production
gulp.task('templatecache', function (done) {
  gulp.src('./www/templates/**/*.html')
    .pipe(templateCache({root: 'templates/', standalone:true}))
    .pipe(gulp.dest('./www/js'))
    .on('end', done);
});

gulp.task('useref', function (done) {
  gulp.src('./www/*.html')
    .pipe(useref())
    .pipe(gulp.dest('./www/dist'))
    .on('end', done);
});
