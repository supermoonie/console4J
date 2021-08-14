const Jvm = {
    /**
     * 获取本地jvm信息
     *
     * @returns {Promise<>}
     */
    getLocalJvm: () => (
        new Promise((resolve, reject) => {
            window.jvmQuery({
                request: "GET_LOCAL_JVM",
                onSuccess: res => {
                    resolve(JSON.parse(res));
                },
                onFailure: (code, error) => {
                    reject({code: code, error: error});
                }
            });
        })
    ),
};

export default Jvm;