const Theme = {
    /**
     * 设置主题
     *
     * @param theme dark or light
     * @returns {Promise<>}
     */
    setTheme: (theme) => (
        new Promise((resolve, reject) => {
            window.themeQuery({
                request: "SET_THEME:" + theme,
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
     * 获取当前主题
     *
     * @returns {Promise<>}
     */
    getTheme: () => (
        new Promise((resolve, reject) => {
            window.themeQuery({
                request: "GET_THEME",
                onSuccess: res => {
                    resolve(res);
                },
                onFailure: (code, error) => {
                    reject({code: code, error: error});
                }
            });
        })
    ),
}

export default Theme;