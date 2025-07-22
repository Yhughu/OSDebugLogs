var exec = cordova.require('cordova/exec');

exports.openConsole = function(success, error) {
    exec(success, error, "OSDebugLogs", "openConsole", []);
};

exports.closeConsole = function(success, error) {
    exec(success, error, "OSDebugLogs", "closeConsole", []);
};
