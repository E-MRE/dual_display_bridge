import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'dual_display_bridge_method_channel.dart';

abstract class DualDisplayBridgePlatform extends PlatformInterface {
  /// Constructs a DualDisplayBridgePlatform.
  DualDisplayBridgePlatform() : super(token: _token);

  static final Object _token = Object();

  static DualDisplayBridgePlatform _instance = MethodChannelDualDisplayBridge();

  /// The default instance of [DualDisplayBridgePlatform] to use.
  ///
  /// Defaults to [MethodChannelDualDisplayBridge].
  static DualDisplayBridgePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [DualDisplayBridgePlatform] when
  /// they register themselves.
  static set instance(DualDisplayBridgePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
