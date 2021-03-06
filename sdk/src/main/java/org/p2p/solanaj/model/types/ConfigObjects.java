package org.p2p.solanaj.model.types;

import java.util.List;

import com.squareup.moshi.Json;

import org.p2p.solanaj.model.types.RpcSendTransactionConfig.Encoding;

public class ConfigObjects {

    public static class ConfirmedSignFAddr2 {
        @Json(name = "limit")
        private long limit;
        @Json(name = "before")
        private String before;
        @Json(name = "until")
        private String until;

        public ConfirmedSignFAddr2(String before, int limit) {
            this.limit = limit;
            this.before = before;
        }
    }

    public static class Memcmp {
        @Json(name = "offset")
        private long offset;
        @Json(name = "bytes")
        private String bytes;

        public Memcmp() {
        }

        public Memcmp(long offset, String bytes) {
            this.offset = offset;
            this.bytes = bytes;
        }

    }

    public static class DataSize {
        public static final int SPAN = 165;
        @Json(name = "dataSize")
        private int dataSize;

        public DataSize(int dataSize) {
            this.dataSize = dataSize;
        }
    }

    public static class Filter {
        @Json(name = "memcmp")
        private Memcmp memcmp;

        public Filter() {
        }

        public Filter(Memcmp memcmp) {
            this.memcmp = memcmp;
        }

    }

    public static class ProgramAccountConfig {
        @Json(name = "encoding")
        private Encoding encoding = null;
        @Json(name = "filters")
        private List<Object> filters = null;

        public ProgramAccountConfig() {
        }

        public ProgramAccountConfig(List<Object> filters) {
            this.filters = filters;
        }

        public ProgramAccountConfig(Encoding encoding) {
            this.encoding = encoding;
        }

    }
}
