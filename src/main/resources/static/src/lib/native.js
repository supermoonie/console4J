let native = {
    notification: {
        /**
         * 创建通知
         *
         * {
         *     title: '',
         *     content: '',
         *     type: 'info/warning/error/null'
         * }
         *
         * @param req
         * @returns {Promise<>}
         */
        createOnce: (req) => (
            new Promise((resolve, reject) => {
                window.notificationQuery({
                    request: "notification:once:create:" + JSON.stringify(req),
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            })
        ),
        /**
         * 创建循环通知
         *
         * {
         *     title: '',
         *     content: '',
         *     type: 'info/warning/error/null'
         *     period: 1,
         *     unit: 'second'
         * }
         *
         * @param req
         * @returns {Promise<>}
         */
        createLoop: (req) => (
            new Promise((resolve, reject) => {
                window.notificationQuery({
                    request: "notification:loop:create:" + JSON.stringify(req),
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            })
        ),
        /**
         * 取消循环通知
         *
         * @returns {Promise<>}
         */
        cancelLoop: () => (
            new Promise((resolve, reject) => {
                window.notificationQuery({
                    request: "notification:loop:cancel",
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            })
        ),
        /**
         * 是否可以取消循环通知，
         * 也可用来验证是否有循环通知的任务存在
         *
         * @returns {Promise<>}
         */
        canCancel: () => (
            new Promise((resolve, reject) => {
                window.notificationQuery({
                    request: "notification:loop:can_cancel",
                    onSuccess: res => {
                        resolve(JSON.parse(res));
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            })
        )
    },
    image: {
        /**
         * 图像压缩
         *
         * [{
         *     quality: 80
         *     speed: 4,
         *     inputPath: '',
         *     outputPath: ''
         * }]
         * @param req
         * [{
         *     success: true,
         *     msg: '',
         *     targetPath: '',
         *     compressedSize: 0
         * }]
         * @returns {Promise<>}
         */
        compress: (req) => (
            new Promise((resolve, reject) => {
                window.imageQuery({
                    request: "image:compress:" + JSON.stringify(req),
                    onSuccess: res => {
                        resolve(JSON.parse(res));
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            })
        ),
        /**
         * 图形大小调整
         *
         * [{
         *     targetWidth: 0,
         *     targetHeight: 0,
         *     targetPercent: 50,
         *     inputPath: '',
         *     outputPath: ''
         * }]
         * @param req
         * {
         *     success: true,
         *     msg: '',
         *     targetPath: '',
         *     width: '',
         *     height: '',
         *     size: ''
         * }
         * @returns {Promise<>}
         */
        resize: req => (
            new Promise((resolve, reject) => {
                window.imageQuery({
                    request: "image:resize:" + JSON.stringify(req),
                    onSuccess: res => {
                        resolve(JSON.parse(res));
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            })
        ),
        /**
         * 图像格式转换
         *
         * [{
         *     inputPath: '',
         *     format: '',
         *     outputPath: ''
         * }]
         * @param req
         * {
         *     success: true,
         *     msg: '',
         *     outputPath: ''
         * }
         * @returns {Promise<>}
         */
        convert: req => (
            new Promise((resolve, reject) => {
                window.imageQuery({
                    request: 'image:convert:' + JSON.stringify(req),
                    onSuccess: res => {
                        resolve(JSON.parse(res));
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            })
        ),
        /**
         * {
         *     path: '',
         *     targetPath: '',
         *     format: 'png'
         * }
         * @param req
         * @returns {Promise<>}
         */
        iconMake: req => (
            new Promise((resolve, reject) => {
                window.imageQuery({
                    request: "image:icon_make:" + JSON.stringify(req),
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            })
        ),
        /**
         * GIF 制作
         * {
         *     outputPath: '',
         *     width: 200,
         *     height: 200,
         *     loopCount: 0,
         *     quality: 10,
         *     frameList: [{
         *         path: '',
         *         delay: 200
         *     }]
         * }
         * @param req
         * @returns {Promise<>}
         */
        gifMake: async req => (
            new Promise((resolve, reject) => {
                window.imageQuery({
                    request: "image:gif_make:" + JSON.stringify(req),
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            })
        ),
    },
    video: {
        /**
         * {
         *      path: '',
         *      start: 0,
         *      end: 10,
         *      width: -1,
         *      height: -1,
         *      frameRate: 5,
         *      quality: 10
         * }
         * @param req
         * @returns {Promise<>}
         */
        toGif: req => (
            new Promise((resolve, reject) => {
                window.videoQuery({
                    request: "video:to_gif:" + JSON.stringify(req),
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                })
            })
        ),
        /**
         *
         * @param path video path
         * {
         *     path: '视频路径',
         *     name: '视频名',
         *     coverPath: '封面图片路径',
         *     mediaInfo: {
         *         format: '视频格式',
         *         duration: '视频长度',
         *         audio: {
         *             decoder: '',
         *             samplingRate: 0,
         *             channels: 0,
         *             bitRate: 0
         *         },
         *         video: {
         *             decoder: '',
         *             size: {
         *                 width: 0,
         *                 height: 0
         *             },
         *             bitRate: 0,
         *             frameRate: -1.0
         *         }
         *     }
         * }
         * @returns {Promise<>}
         */
        info: path => (
            new Promise((resolve, reject) => {
                window.videoQuery({
                    request: "video:info:" + path,
                    onSuccess: res => {
                        resolve(JSON.parse(res));
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            })
        ),
        /**
         * 播放视频
         *
         * @param path 视频路径
         * @returns {Promise<>}
         */
        play: path => (
            new Promise((resolve, reject) => {
                window.videoQuery({
                    request: "video:play:" + path,
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            })
        )
    },
    jWindow: {
        /**
         * 设置窗口大小
         *
         * @param width 宽度
         * @param height 高度
         * @returns {Promise<>}
         */
        setWindowSize: (width, height) => (
            new Promise((resolve, reject) => {
                window.windowQuery({
                    request: "setWindowSize" + JSON.stringify({
                        "width": width,
                        "height": height
                    }),
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            })
        )
    },
    computerHostAddress: async () => {
        return await function () {
            return new Promise((resolve, reject) => {
                window.computerQuery({
                    request: "computer:host_address:",
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            });
        }();
    },
    isNative: function () {
        return window.hasOwnProperty('computerQuery');
    },
    gifSplit: async req => {
        return await function () {
            return new Promise((resolve, reject) => {
                window.imageQuery({
                    request: "image:gif_split:" + JSON.stringify(req),
                    onSuccess: res => {
                        resolve(JSON.parse(res));
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            });
        }();
    },
    fileMove: async req => {
        return await function () {
            return new Promise((resolve, reject) => {
                window.fileQuery({
                    request: "file:move:" + JSON.stringify(req),
                    onSuccess: function (res) {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            });
        }();
    },
    readText: async path => {
        return await function () {
            return new Promise((resolve, reject) => {
                window.fileQuery({
                    request: "file:read:text:" + path,
                    onSuccess: function (res) {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            });
        }();
    },
    saveText: async req => {
        return await function () {
            return new Promise((resolve, reject) => {
                window.fileQuery({
                    request: "file:save:text:" + JSON.stringify(req),
                    onSuccess: function (res) {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            });
        }();
    },
    file: {
        /**
         * 获取用户家目录
         *
         * @returns {Promise<>}
         */
        userHome: () => {
            return new Promise(((resolve, reject) => {
                window.fileQuery({
                    request: "file:user_home",
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            }))
        },
        /**
         * 选择目录
         *
         * @param defaultPath
         * @returns {Promise<>}
         */
        folderSelect: (defaultPath) => (
            new Promise((resolve, reject) => {
                window.fileQuery({
                    request: "file:folder_select:" + defaultPath,
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            })
        ),
        /**
         * 文件/文件夹选择弹框
         * {
         *     "selectType": 1,
         *     "defaultFilePath": null,
         *     "title": "Open",
         *     "isImage": true,
         *     "extensionFilter": [
         *          "|.png;.jpeg;.jpg"
         *     ]
         * }
         * @param req
         * @returns {Promise<>}
         */
        dialog: req => (
            new Promise((resolve, reject) => {
                window.fileQuery({
                    request: "file:dialog:" + JSON.stringify(req),
                    onSuccess: function (res) {
                        resolve(JSON.parse(res));
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            })
        )
    },
    openFolder: async path => {
        return await function () {
            return new Promise(((resolve, reject) => {
                window.fileQuery({
                    request: "file:open_directory:" + path,
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            }))
        }();
    },
    parseHtml: async (req) => {
        return await function () {
            return new Promise(((resolve, reject) => {
                window.htmlParseQuery({
                    request: "html:parse:" + JSON.stringify(req),
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            }))
        }();
    },
    videoDownload: async (req) => {
        return await function () {
            return new Promise(((resolve, reject) => {
                window.videoDownloadQuery({
                    request: "video:download:" + JSON.stringify(req),
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            }))
        }();
    },
    videoCustomDownload: async (req) => {
        return await function () {
            return new Promise(((resolve, reject) => {
                window.videoDownloadQuery({
                    request: "video:download:custom:" + JSON.stringify(req),
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            }))
        }();
    },
    computeQtl: async (req) => {
        return await function () {
            return new Promise(((resolve, reject) => {
                window.computeQuery({
                    request: "compute:qtl:" + JSON.stringify(req),
                    onSuccess: res => {
                        resolve(JSON.parse(res));
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            }))
        }();
    },
    computeStringFormatInteger: async (req) => {
        return await function () {
            return new Promise(((resolve, reject) => {
                window.computeQuery({
                    request: "compute:format:string_to_integer:" + JSON.stringify(req),
                    onSuccess: res => {
                        resolve(JSON.parse(res));
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            }))
        }();
    },
    copyText: async (req) => {
        return await function () {
            return new Promise(((resolve, reject) => {
                window.clipboardQuery({
                    request: "clipboard:copy:text:" + req,
                    onSuccess: res => {
                        resolve(undefined);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            }))
        }();
    },
    preferences: {
        getString: (key) => {
            return new Promise(((resolve, reject) => {
                window.preferencesQuery({
                    request: "preferences:get:string:" + key,
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            }))
        },
        setString: (key, value) => {
            return new Promise(((resolve, reject) => {
                window.preferencesQuery({
                    request: "preferences:set:string:" + JSON.stringify({
                        "key": key,
                        "value": value
                    }),
                    onSuccess: res => {
                        resolve(res);
                    },
                    onFailure: (code, error) => {
                        reject({code: code, error: error});
                    }
                });
            }))
        }
    }
};

export default native;