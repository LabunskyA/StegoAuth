JC = javac
JAR = jar

BUILD_DIR = build
SRC_DIR = src
META_DIR = META-INF

HQ_MAIN = info/labunsky/stego/auth/HQUtil.java
SPY_MAIN = info/labunsky/stego/auth/SpyUtil.java

HQ_JAR = HQUtil.jar
HQ_MF = $(META_DIR)/HQ.mf

SPY_JAR = SpyUtil.jar
SPY_MF = $(META_DIR)/Spy.mf


all: hq-util spy-util

hq-prepare:
	mkdir -p build/hq

hq-util: hq-prepare
	$(JC) -d $(BUILD_DIR)/hq -sourcepath $(SRC_DIR) $(SRC_DIR)/$(HQ_MAIN)
	$(JAR) cfm $(HQ_JAR) $(HQ_MF) -C $(BUILD_DIR)/hq .

spy-prepare:
	mkdir -p build/spy

spy-util: spy-prepare
	$(JC) -d $(BUILD_DIR)/spy -sourcepath $(SRC_DIR) $(SRC_DIR)/$(SPY_MAIN)
	$(JAR) cfm $(SPY_JAR) $(SPY_MF) -C $(BUILD_DIR)/spy .

clean:
	rm -rf $(BUILD_DIR)

.PHONY: all classes