import 'package:flutter_test/flutter_test.dart';
import 'package:dual_display_bridge/dual_display_bridge.dart';
import 'package:dual_display_bridge/dual_display_bridge_platform_interface.dart';
import 'package:dual_display_bridge/dual_display_bridge_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockDualDisplayBridgePlatform
    with MockPlatformInterfaceMixin
    implements DualDisplayBridgePlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final DualDisplayBridgePlatform initialPlatform = DualDisplayBridgePlatform.instance;

  test('$MethodChannelDualDisplayBridge is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelDualDisplayBridge>());
  });

  test('getPlatformVersion', () async {
    DualDisplayBridge dualDisplayBridgePlugin = DualDisplayBridge();
    MockDualDisplayBridgePlatform fakePlatform = MockDualDisplayBridgePlatform();
    DualDisplayBridgePlatform.instance = fakePlatform;

    expect(await dualDisplayBridgePlugin.getPlatformVersion(), '42');
  });
}
