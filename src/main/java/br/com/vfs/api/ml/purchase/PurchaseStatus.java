package br.com.vfs.api.ml.purchase;

public enum PurchaseStatus {
    STARTED, FINALLY;

    public boolean isFinally() {
        return this.equals(FINALLY);
    }
}
