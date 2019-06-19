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

LIB_JAR = StegoAuth.jar


all: lib hq-util spy-util

hq-prepare:
	mkdir -p $(BUILD_DIR)/hq
	$(JC) -d $(BUILD_DIR)/hq -sourcepath $(SRC_DIR) $(SRC_DIR)/$(HQ_MAIN)

hq-util: hq-prepare
	$(JAR) cfm $(HQ_JAR) $(HQ_MF) -C $(BUILD_DIR)/hq .

spy-prepare:
	mkdir -p $(BUILD_DIR)/spy
	$(JC) -d $(BUILD_DIR)/spy -sourcepath $(SRC_DIR) $(SRC_DIR)/$(SPY_MAIN)

spy-util: spy-prepare
	$(JAR) cfm $(SPY_JAR) $(SPY_MF) -C $(BUILD_DIR)/spy .

lib-prepare: spy-prepare hq-prepare
	mkdir -p $(BUILD_DIR)/lib
	cp -r $(BUILD_DIR)/hq/. $(BUILD_DIR)/lib/
	cp -r $(BUILD_DIR)/spy/. $(BUILD_DIR)/lib/

lib: lib-prepare
	$(JAR) cvf $(LIB_JAR) -C $(BUILD_DIR)/lib .


clean:
	rm -rf $(BUILD_DIR)

.PHONY: all spy-prepare hq-prepare lib-prepare