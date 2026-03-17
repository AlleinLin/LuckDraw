package io.prizewheel.core.port.input;

import java.util.Map;

/**
 * 规则引擎服务端口
 * 
 * @author Allein
 * @since 2.0.0
 */
public interface RuleEnginePort {

    Long evaluate(Long treeId, Map<String, String> userAttributes);

    Long evaluate(String treeName, Map<String, String> userAttributes);

    String evaluateAndReturnNodeValue(Long treeId, Map<String, String> userAttributes);
}
