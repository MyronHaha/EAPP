
package com.hyhscm.myron.eapp.data;

public class AppInfo {

    private String name;
    private String version;
    private String changelog;
    private long updated_at;
    private String versionShort;
    private String build;
    private String installUrl;
    private String install_url;
    private String direct_install_url;
    private String update_url;
    private Binary binary;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public String getVersion() {
        return version;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }
    public String getChangelog() {
        return changelog;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }
    public long getUpdated_at() {
        return updated_at;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }
    public String getVersionShort() {
        return versionShort;
    }

    public void setBuild(String build) {
        this.build = build;
    }
    public String getBuild() {
        return build;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }
    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }
    public String getInstall_url() {
        return install_url;
    }

    public void setDirect_install_url(String direct_install_url) {
        this.direct_install_url = direct_install_url;
    }
    public String getDirect_install_url() {
        return direct_install_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }
    public String getUpdate_url() {
        return update_url;
    }

    public void setBinary(Binary binary) {
        this.binary = binary;
    }
    public Binary getBinary() {
        return binary;
    }


    /**
     * Auto-generated: 2018-03-08 17:17:9
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Binary {

        private long fsize;
        public void setFsize(long fsize) {
            this.fsize = fsize;
        }
        public long getFsize() {
            return fsize;
        }

    }
}