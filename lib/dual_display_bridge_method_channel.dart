import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'dual_display_bridge_platform_interface.dart';

/// An implementation of [DualDisplayBridgePlatform] that uses method channels.
class MethodChannelDualDisplayBridge extends DualDisplayBridgePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('dual_display_bridge');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
